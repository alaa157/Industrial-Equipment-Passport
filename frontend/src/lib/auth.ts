export type AuthResponse={
  accessToken:string;
  refreshToken:string;
  expiresInSeconds:number;
  userId:string;
  username:string;
  roles:string[];
};

const ACCESS_TOKEN_KEY="iep_access_token";
const REFRESH_TOKEN_KEY="iep_refresh_token";

export function saveAuth(auth:AuthResponse){
  localStorage.setItem(ACCESS_TOKEN_KEY,auth.accessToken);
  localStorage.setItem(REFRESH_TOKEN_KEY,auth.refreshToken);
}

export function getAccessToken(){
  if(typeof window==="undefined")return null;
  return localStorage.getItem(ACCESS_TOKEN_KEY);
}

export function getRefreshToken(){
  if(typeof window==="undefined")return null;
  return localStorage.getItem(REFRESH_TOKEN_KEY);
}

export function clearAuth(){
  localStorage.removeItem(ACCESS_TOKEN_KEY);
  localStorage.removeItem(REFRESH_TOKEN_KEY);
}
