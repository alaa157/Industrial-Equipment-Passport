"use client";

import {useQuery} from "@tanstack/react-query";
import {ShieldCheck} from "lucide-react";
import {AppShell} from "@/components/app-shell";
import {PageHeader} from "@/components/page-header";
import {apiFetch} from "@/lib/api";

export default function Audit(){
const query=useQuery({queryKey:["audit"],queryFn:()=>apiFetch<any>("/api/v1/audit?size=100")});

return <AppShell>
<PageHeader eyebrow="TRACEABILITY" title="Audit history" description="Operational event history generated from domain activity."/>
<div className="overflow-hidden rounded-2xl border bg-white shadow-sm">
<div className="overflow-x-auto">
<table className="w-full min-w-[850px] text-left"><thead className="border-b bg-slate-50 text-xs uppercase tracking-wide text-slate-500"><tr><th className="px-5 py-4">Time</th><th className="px-5 py-4">Action</th><th className="px-5 py-4">Entity</th><th className="px-5 py-4">Entity ID</th><th className="px-5 py-4">Payload</th></tr></thead><tbody className="divide-y">
{query.data?.content?.map((x:any)=><tr key={x.id}><td className="px-5 py-4 text-sm">{new Date(x.timestamp).toLocaleString()}</td><td className="px-5 py-4"><span className="rounded-full bg-slate-100 px-3 py-1 text-xs font-semibold">{x.action}</span></td><td className="px-5 py-4 text-sm">{x.entityType}</td><td className="px-5 py-4 font-mono text-xs">{x.entityId??"—"}</td><td className="max-w-md truncate px-5 py-4 font-mono text-xs text-slate-500">{x.newValues??"—"}</td></tr>)}
{!query.data?.content?.length&&!query.isLoading&&<tr><td colSpan={5} className="px-5 py-12 text-center text-slate-400"><ShieldCheck className="mx-auto mb-2"/>No audit activity yet.</td></tr>}
</tbody></table></div></div>
</AppShell>;
}
