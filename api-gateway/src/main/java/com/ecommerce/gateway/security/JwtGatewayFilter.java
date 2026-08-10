package com.ecommerce.gateway.security;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

/**
 * GlobalFilter körs på requests som går genom gatewayn.
 *
 * Denna filter kontrollerar JWT-token för skyddade routes
 * och hanterar grundläggande rollbehörighet.
 */
@Component
@RequiredArgsConstructor
public class JwtGatewayFilter implements GlobalFilter, Ordered {

    private final JwtService jwtService;

    /**
     * Själva filter-logiken.
     */
    @Override
    public Mono<Void> filter(
            ServerWebExchange exchange,
            GatewayFilterChain chain
    ) {

        ServerHttpRequest request = exchange.getRequest();

        /*
         * 1. Publika endpoints får användas utan token.
         *
         * Auth är publik.
         * GET mot products är publik.
         */
        if (isPublicRequest(request)) {
            return chain.filter(exchange);
        }

        /*
         * 2. Alla andra endpoints kräver JWT-token.
         */
        String authHeader = request
                .getHeaders()
                .getFirst("Authorization");

        /*
         * Om Authorization-header saknas eller inte börjar
         * med "Bearer " stoppas requesten direkt.
         */
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return unauthorized(exchange);
        }

        String token = authHeader.substring(7);

        /*
         * Kontrollera att JWT-token är giltig.
         */
        if (!jwtService.isTokenValid(token)) {
            return unauthorized(exchange);
        }

        /*
         * 3. Läs claims från JWT-token.
         */
        Claims claims = jwtService.extractClaims(token);

        String username = claims.getSubject();
        String userId = String.valueOf(claims.get("userId"));
        String role = String.valueOf(claims.get("role"));

        /*
         * 4. Dessa inventory-endpoints är endast avsedda
         * för intern kommunikation mellan mikrotjänster.
         *
         * De ska därför aldrig kunna anropas genom gatewayn,
         * inte ens av ADMIN.
         */
        if (isInternalInventoryEndpoint(request)) {
            return forbidden(exchange);
        }

        /*
         * 5. Kontrollera endpoints som kräver ADMIN.
         *
         * Exempel:
         * POST /products
         * PUT /products/{id}
         * DELETE /products/{id}
         * POST /inventory
         */
        if (requiresAdmin(request)
                && !"ROLE_ADMIN".equals(role)) {

            return forbidden(exchange);
        }

        /*
         * 6. Skicka användarinformation vidare
         * till den bakomliggande mikrotjänsten.
         */
        ServerHttpRequest mutatedRequest = request
                .mutate()
                .header("X-User-Name", username)
                .header("X-User-Id", userId)
                .header("X-User-Role", role)
                .build();

        return chain.filter(
                exchange.mutate()
                        .request(mutatedRequest)
                        .build()
        );
    }

    /**
     * Bestämmer vilka requests som är publika.
     */
    private boolean isPublicRequest(ServerHttpRequest request) {

        String path = request.getURI().getPath();
        HttpMethod method = request.getMethod();

        /*
         * Login och register är publika.
         */
        if (path.startsWith("/api/v1/auth/")) {
            return true;
        }

        /*
         * Produkter får hämtas utan login.
         *
         * GET /api/v1/products
         * GET /api/v1/products/1
         */
        if (path.startsWith("/api/v1/products")
                && HttpMethod.GET.equals(method)) {

            return true;
        }

        return false;
    }

    /**
     * Bestämmer vilka requests som kräver ROLE_ADMIN.
     */
    private boolean requiresAdmin(ServerHttpRequest request) {

        String path = request.getURI().getPath();
        HttpMethod method = request.getMethod();

        /*
         * Alla ändringar av produkter kräver ADMIN.
         *
         * POST
         * PUT
         * PATCH
         * DELETE
         */
        if (path.startsWith("/api/v1/products")
                && !HttpMethod.GET.equals(method)) {

            return true;
        }

        /*
         * Att skapa inventory manuellt kräver ADMIN.
         */
        if (path.equals("/api/v1/inventory")
                && HttpMethod.POST.equals(method)) {

            return true;
        }

        return false;
    }

    /**
     * Endpoints som endast ska användas internt
     * mellan mikrotjänster.
     */
    private boolean isInternalInventoryEndpoint(
            ServerHttpRequest request
    ) {

        String path = request.getURI().getPath();

        return path.equals("/api/v1/inventory/reserve")
                || path.equals("/api/v1/inventory/release")
                || path.equals("/api/v1/inventory/confirm");
    }

    /**
     * Returnerar 401 Unauthorized.
     *
     * Används när token saknas eller är ogiltig.
     */
    private Mono<Void> unauthorized(ServerWebExchange exchange) {

        exchange.getResponse()
                .setStatusCode(HttpStatus.UNAUTHORIZED);

        return exchange.getResponse().setComplete();
    }

    /**
     * Returnerar 403 Forbidden.
     *
     * Används när användaren är inloggad men
     * saknar rätt behörighet.
     */
    private Mono<Void> forbidden(ServerWebExchange exchange) {

        exchange.getResponse()
                .setStatusCode(HttpStatus.FORBIDDEN);

        return exchange.getResponse().setComplete();
    }

    /**
     * Lägre värde betyder att filtret körs tidigare.
     */
    @Override
    public int getOrder() {
        return -1;
    }
}