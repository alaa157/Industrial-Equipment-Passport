"use client";

import {getAccessToken} from "@/lib/auth";

export type CurrentUser={
id:string;
username:string;
roles:string[];
permissions:string[];
};

function decodePayload(token:string){
try{
const middle=token.split(".")[1];
return JSON.parse(atob(middle.replace(/-/g,"+").replace(/_/g,"/")));
}catch{
return null;
}
}

export function getCurrentUser():CurrentUser|null{
const token=getAccessToken();
if(!token)return null;
const payload=decodePayload(token);
if(!payload?.sub)return null;
return{
id:payload.sub,
username:payload.username??"User",
roles:Array.isArray(payload.roles)?payload.roles:[],
permissions:Array.isArray(payload.permissions)?payload.permissions:[]
};
}
