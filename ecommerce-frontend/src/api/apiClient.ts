export async function apiClient<T>(
    url: string,
    options?: RequestInit
): Promise<T> {
    const response = await fetch(url, options);

    if(!response.ok){
        throw new Error(
            `Request failed with status ${response.status}`
        );
    }
    return response.json() as Promise<T>
}