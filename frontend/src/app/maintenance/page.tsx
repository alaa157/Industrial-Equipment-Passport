"use client";

import Link from "next/link";
import {useQuery} from "@tanstack/react-query";
import {Plus,Search,Wrench,ChevronLeft,ChevronRight} from "lucide-react";
import {useState} from "react";
import {AppShell} from "@/components/app-shell";
import {PageHeader} from "@/components/page-header";
import {StatusBadge} from "@/components/status-badge";
import {apiFetch} from "@/lib/api";

export default function MaintenancePage(){
const [query,setQuery]=useState("");
const [status,setStatus]=useState("");
const [priority,setPriority]=useState("");
const [page,setPage]=useState(0);

const result=useQuery({
queryKey:["maintenance-search",query,status,priority,page],
queryFn:()=>apiFetch<any>(`/api/v1/maintenance/search?query=${encodeURIComponent(query)}&status=${status}&priority=${priority}&page=${page}&size=15`)
});

return <AppShell>
<PageHeader eyebrow="SERVICE MANAGEMENT" title="Maintenance" description="Corrective, preventive, scheduled and emergency work." action={<Link href="/maintenance/new" className="inline-flex items-center gap-2 rounded-xl bg-slate-950 px-4 py-3 text-sm font-semibold text-white"><Plus size={18}/>New request</Link>}/>
<div className="mb-5 rounded-2xl border bg-white p-4 shadow-sm">
<div className="grid gap-3 lg:grid-cols-[1fr_180px_180px]">
<div className="relative"><Search size={17} className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400"/><input value={query} onChange={e=>{setQuery(e.target.value);setPage(0)}} placeholder="Search work orders..." className="w-full rounded-xl border px-10 py-3 outline-none focus:border-cyan-600"/></div>
<select value={status} onChange={e=>{setStatus(e.target.value);setPage(0)}} className="rounded-xl border px-4"><option value="">All statuses</option><option value="OPEN">Open</option><option value="ASSIGNED">Assigned</option><option value="IN_PROGRESS">In progress</option><option value="ON_HOLD">On hold</option><option value="COMPLETED">Completed</option><option value="CANCELLED">Cancelled</option></select>
<select value={priority} onChange={e=>{setPriority(e.target.value);setPage(0)}} className="rounded-xl border px-4"><option value="">All priorities</option><option value="LOW">Low</option><option value="MEDIUM">Medium</option><option value="HIGH">High</option><option value="CRITICAL">Critical</option></select>
</div>
</div>

<div className="overflow-hidden rounded-2xl border bg-white shadow-sm">
<div className="overflow-x-auto">
<table className="w-full min-w-[850px] text-left">
<thead className="border-b bg-slate-50 text-xs uppercase tracking-wide text-slate-500"><tr><th className="px-5 py-4">Request</th><th className="px-5 py-4">Equipment</th><th className="px-5 py-4">Type</th><th className="px-5 py-4">Priority</th><th className="px-5 py-4">Status</th><th className="px-5 py-4">Due</th></tr></thead>
<tbody className="divide-y">
{result.isLoading&&<tr><td colSpan={6} className="px-5 py-10 text-center text-slate-400">Loading work orders...</td></tr>}
{result.data?.content?.map((m:any)=><tr key={m.id} className="hover:bg-slate-50">
<td className="px-5 py-4"><Link href={`/maintenance/${m.id}`} className="font-semibold text-cyan-700 hover:underline">{m.title}</Link><div className="text-xs text-slate-400">{m.id}</div></td>
<td className="px-5 py-4 font-mono text-xs">{m.equipmentId}</td>
<td className="px-5 py-4 text-sm">{m.type}</td>
<td className="px-5 py-4"><StatusBadge value={m.priority}/></td>
<td className="px-5 py-4"><StatusBadge value={m.status}/></td>
<td className="px-5 py-4 text-sm">{m.dueDate??"—"}</td>
</tr>)}
{!result.isLoading&&!result.data?.content?.length&&<tr><td colSpan={6} className="px-5 py-10 text-center text-slate-400"><Wrench className="mx-auto mb-2"/>No work orders found.</td></tr>}
</tbody>
</table>
</div>
<div className="flex items-center justify-between border-t px-5 py-4 text-sm"><span className="text-slate-500">{result.data?.totalElements??0} requests</span><div className="flex items-center gap-2"><button disabled={page===0} onClick={()=>setPage(x=>x-1)} className="rounded-lg border p-2 disabled:opacity-30"><ChevronLeft size={16}/></button><span>Page {page+1} / {result.data?.totalPages??1}</span><button disabled={page+1>=(result.data?.totalPages??1)} onClick={()=>setPage(x=>x+1)} className="rounded-lg border p-2 disabled:opacity-30"><ChevronRight size={16}/></button></div></div>
</div>
</AppShell>;
}
