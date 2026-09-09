"use client";

import {useEffect} from "react";
import {useRouter} from "next/navigation";
import {getAccessToken} from "@/lib/auth";

export default function RootPage(){
const router=useRouter();

useEffect(()=>{
router.replace(getAccessToken()?"/dashboard":"/login");
},[router]);

return <main className="flex min-h-screen items-center justify-center bg-slate-100 text-sm text-slate-500">Opening Industrial Equipment Passport...</main>;
}
