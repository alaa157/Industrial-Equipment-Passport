import {
  clearAuth,
  getAccessToken,
  getRefreshToken,
  saveAuth,
  AuthResponse,
} from "@/lib/auth";

const API_URL = process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8080";

async function rawFetch(
  path: string,
  options: RequestInit = {},
  token?: string,
) {
  return fetch(`${API_URL}${path}`, {
    ...options,
    headers: {
      ...(options.body instanceof FormData
        ? {}
        : { "Content-Type": "application/json" }),
      ...(token ? { Authorization: `Bearer ${token}` } : {}),
      ...(options.headers ?? {}),
    },
    cache: "no-store",
  });
}

export async function apiFetch<T>(
  path: string,
  options: RequestInit = {},
): Promise<T> {
  let token = getAccessToken();

  let response: Response;

  try {
    response = await rawFetch(path, options, token ?? undefined);
  } catch (error) {
    throw new Error(
      "Unable to reach the API server. Check that the API gateway is running and CORS is configured correctly.",
    );
  }

  if (response.status === 401 && getRefreshToken()) {
    try {
      const refreshResponse = await rawFetch("/api/v1/auth/refresh", {
        method: "POST",
        body: JSON.stringify({
          refreshToken: getRefreshToken(),
        }),
      });

      if (refreshResponse.ok) {
        const auth = (await refreshResponse.json()) as AuthResponse;

        saveAuth(auth);

        response = await rawFetch(path, options, auth.accessToken);
      }
    } catch {
      clearAuth();
      throw new Error("Your session has expired. Please sign in again.");
    }
  }

  if (!response.ok) {
    let message = `Request failed (${response.status})`;

    try {
      const body = await response.json();

      if (body?.message) {
        message = body.message;
      }
    } catch {
      // Response wasn't JSON.
    }

    if (response.status === 401) {
      clearAuth();
      message = "Your session has expired. Please sign in again.";
    }

    throw new Error(message);
  }

  if (response.status === 204) {
    return undefined as T;
  }

  return response.json() as Promise<T>;
}

export async function apiBlob(path: string): Promise<Blob> {
  const token = getAccessToken();
  const response = await rawFetch(path, {}, token ?? undefined);
  if (!response.ok) throw new Error("Unable to download resource");
  return response.blob();
}
