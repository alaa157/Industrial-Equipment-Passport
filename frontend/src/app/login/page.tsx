"use client";

import {useState} from "react";
import {useRouter} from "next/navigation";
import {useForm} from "react-hook-form";
import {z} from "zod";
import {zodResolver} from "@hookform/resolvers/zod";
import {apiFetch} from "@/lib/api";
import {AuthResponse,saveAuth} from "@/lib/auth";

const schema=z.object({
usernameOrEmail:z.string().min(1,"Username or email is required"),
password:z.string().min(1,"Password is required")
});

type Form=z.infer<typeof schema>;

export default function LoginPage(){
const router=useRouter();
const [error,setError]=useState("");
const [loading,setLoading]=useState(false);
const {register,handleSubmit,formState:{errors}}=useForm<Form>({resolver:zodResolver(schema)});

async function submit(values:Form){
setError("");setLoading(true);
try{
const auth=await apiFetch<AuthResponse>("/api/v1/auth/login",{method:"POST",body:JSON.stringify(values)});
saveAuth(auth);
router.replace("/dashboard");
}catch(e){setError(e instanceof Error?e.message:"Unable to sign in");}
finally{setLoading(false);}
}

return <main className="min-h-screen grid lg:grid-cols-[1.15fr_.85fr]">
<section className="hidden bg-slate-950 p-16 text-white lg:flex lg:flex-col lg:justify-between">
<div>
<div className="text-xs font-bold tracking-[0.25em] text-cyan-400">INDUSTRIAL EQUIPMENT PASSPORT</div>
<h1 className="mt-12 max-w-2xl text-6xl font-bold leading-[1.05]">Know every machine. Remember every event.</h1>
<p className="mt-7 max-w-xl text-lg leading-8 text-slate-300">A digital equipment passport for lifecycle management, maintenance, inspections, downtime and industrial traceability.</p>
</div>
<div className="text-sm text-slate-500">Operations management platform</div>
</section>

<section className="flex items-center justify-center bg-slate-100 p-6">
<div className="w-full max-w-md rounded-2xl border border-slate-200 bg-white p-8 shadow-xl">
<div className="mb-8"><div className="text-xs font-bold tracking-[0.2em] text-cyan-700">SECURE ACCESS</div><h2 className="mt-2 text-3xl font-bold">Sign in</h2><p className="mt-2 text-sm text-slate-500">Enter your organization credentials.</p></div>
<form onSubmit={handleSubmit(submit)} className="space-y-5">
<div><label className="mb-2 block text-sm font-medium">Username or email</label><input {...register("usernameOrEmail")} className="w-full rounded-xl border border-slate-300 px-4 py-3 outline-none focus:border-cyan-600"/>{errors.usernameOrEmail&&<p className="mt-1 text-xs text-red-600">{errors.usernameOrEmail.message}</p>}</div>
<div><label className="mb-2 block text-sm font-medium">Password</label><input type="password" {...register("password")} className="w-full rounded-xl border border-slate-300 px-4 py-3 outline-none focus:border-cyan-600"/>{errors.password&&<p className="mt-1 text-xs text-red-600">{errors.password.message}</p>}</div>
{error&&<div className="rounded-xl border border-red-200 bg-red-50 p-3 text-sm text-red-700">{error}</div>}
<button disabled={loading} className="w-full rounded-xl bg-slate-950 px-4 py-3 font-semibold text-white disabled:opacity-50">{loading?"Signing in...":"Sign in"}</button>
</form>
<div className="mt-6 rounded-xl bg-slate-50 p-4 text-xs text-slate-500">Development account: <strong>admin</strong> / <strong>Admin123!ChangeMe</strong></div>
</div>
</section>
</main>;
}
