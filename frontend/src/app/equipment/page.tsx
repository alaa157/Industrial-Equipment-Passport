"use client";

import Link from "next/link";
import {useState} from "react";
import {useQuery} from "@tanstack/react-query";
import {Boxes,ChevronLeft,ChevronRight,Plus,Search} from "lucide-react";
import {AppShell} from "@/components/app-shell";
import {apiFetch} from "@/lib/api";
import {Equipment,EquipmentStatus} from "@/lib/types";

type PageResponse={content:Equipment[];number:number;size:number;totalPages:number;totalElements:number};

export default function EquipmentPage(){
const [query,setQuery]=useState("");
const [status,setStatus]=useState<EquipmentStatus|"" >("");
const [page,setPage]=useState(0);

const result=useQuery({
queryKey:["equipment",query,status,page],
queryFn:()=>apiFetch<PageResponse>(`/api/v1/equipment?query=${encodeURIComponent(query)}&status=${status}&page=${page}&size=15&sort=createdAt&direction=desc`)
});

return <AppShell>
<div className="mb-6 flex flex-col justify-between gap-4 sm:flex-row sm:items-end">
<div>
<div className="text-xs font-bold tracking-[0.2em] text-cyan-700">ASSET REGISTER</div>
<h1 className="mt-2 text-3xl font-bold">Equipment</h1>
<p className="mt-1 text-slate-500">Search and manage registered industrial assets.</p>
</div>
<Link href="/equipment/new" className="inline-flex items-center justify-center gap-2 rounded-xl bg-slate-950 px-4 py-3 text-sm font-semibold text-white hover:bg-slate-800"><Plus size={18}/>Register equipment</Link>
</div>

<div className="mb-5 rounded-2xl border border-slate-200 bg-white p-4 shadow-sm">
<div className="grid gap-3 md:grid-cols-[1fr_220px]">
<div className="relative">
<Search className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" size={18}/>
<input value={query} onChange={e=>{setQuery(e.target.value);setPage(0)}} placeholder="Search asset code, serial, manufacturer..." className="w-full rounded-xl border border-slate-300 py-3 pl-10 pr-4 outline-none focus:border-cyan-600"/>
</div>
<select value={status} onChange={e=>{setStatus(e.target.value as EquipmentStatus|"");setPage(0)}} className="rounded-xl border border-slate-300 px-4 py-3 outline-none focus:border-cyan-600">
<option value="">All statuses</option>
<option value="ACTIVE">Active</option>
<option value="UNDER_MAINTENANCE">Under maintenance</option>
<option value="OUT_OF_SERVICE">Out of service</option>
<option value="RETIRED">Retired</option>
<option value="DECOMMISSIONED">Decommissioned</option>
</select>
</div>
</div>

<div className="overflow-hidden rounded-2xl border border-slate-200 bg-white shadow-sm">
<div className="overflow-x-auto">
<table className="w-full min-w-[850px] text-left">
<thead className="border-b border-slate-200 bg-slate-50 text-xs uppercase tracking-wide text-slate-500">
<tr><th className="px-5 py-4">Asset</th><th className="px-5 py-4">Equipment</th><th className="px-5 py-4">Manufacturer</th><th className="px-5 py-4">Location</th><th className="px-5 py-4">Status</th><th className="px-5 py-4">Criticality</th></tr>
</thead>
<tbody className="divide-y divide-slate-100">
{result.isLoading&&<tr><td colSpan={6} className="px-5 py-10 text-center text-slate-500">Loading equipment...</td></tr>}
{!result.isLoading&&!result.data?.content.length&&<tr><td colSpan={6} className="px-5 py-10 text-center text-slate-500"><Boxes className="mx-auto mb-2" size={28}/>No equipment found.</td></tr>}
{result.data?.content.map(item=><tr key={item.id} className="hover:bg-slate-50">
<td className="px-5 py-4"><Link href={`/equipment/${item.id}`} className="font-semibold text-cyan-700 hover:underline">{item.assetCode}</Link><div className="text-xs text-slate-400">{item.serialNumber}</div></td>
<td className="px-5 py-4"><div className="font-medium">{item.name}</div><div className="text-xs text-slate-500">{item.equipmentType}</div></td>
<td className="px-5 py-4 text-sm">{item.manufacturer}<div className="text-xs text-slate-400">{item.model}</div></td>
<td className="px-5 py-4 text-sm">{item.site??"—"}<div className="text-xs text-slate-400">{item.area??"Unassigned"}</div></td>
<td className="px-5 py-4"><span className="rounded-full bg-slate-100 px-3 py-1 text-xs font-semibold">{item.status.replaceAll("_"," ")}</span></td>
<td className="px-5 py-4"><span className={`rounded-full px-3 py-1 text-xs font-semibold ${item.criticality==="CRITICAL"?"bg-red-100 text-red-700":item.criticality==="HIGH"?"bg-amber-100 text-amber-700":"bg-slate-100 text-slate-700"}`}>{item.criticality}</span></td>
</tr>)}
</tbody>
</table>
</div>

<div className="flex items-center justify-between border-t border-slate-200 px-5 py-4 text-sm">
<div className="text-slate-500">{result.data?.totalElements??0} assets</div>
<div className="flex items-center gap-2">
<button disabled={page===0} onClick={()=>setPage(p=>p-1)} className="rounded-lg border p-2 disabled:opacity-40"><ChevronLeft size={17}/></button>
<span className="min-w-20 text-center">Page {page+1} / {result.data?.totalPages??1}</span>
<button disabled={page+1>=(result.data?.totalPages??1)} onClick={()=>setPage(p=>p+1)} className="rounded-lg border p-2 disabled:opacity-40"><ChevronRight size={17}/></button>
</div>
</div>
</div>
</AppShell>;
}
