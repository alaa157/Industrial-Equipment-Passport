"use client";

import Link from "next/link";
import {useParams} from "next/navigation";
import {useMutation,useQuery,useQueryClient} from "@tanstack/react-query";
import {AlertTriangle,CalendarClock,Download,Edit3,FileText,History,Package,Plus,QrCode,Wrench} from "lucide-react";
import {AppShell} from "@/components/app-shell";
import {AttachmentPanel} from "@/components/attachment-panel";
import {StatusBadge} from "@/components/status-badge";
import {apiBlob,apiFetch} from "@/lib/api";
import {useEquipment} from "@/lib/hooks";
import {useState} from "react";

type Inspection={id:string;inspectionDate:string;checklist:string;result:string;notes?:string;nextInspectionDate?:string;};
type Fault={id:string;title:string;description:string;severity:string;status:string;reportedAt:string;};

const tabs=["Overview","Maintenance","Inspections","Parts","Downtime","Faults","Attachments","Audit History"];

export default function EquipmentDetails(){
const params=useParams<{id:string}>();
const id=params.id;
const queryClient=useQueryClient();
const equipment=useEquipment(id);
const [tab,setTab]=useState("Overview");

const inspections=useQuery({queryKey:["inspections",id],queryFn:()=>apiFetch<Inspection[]>(`/api/v1/equipment/${id}/inspections`),enabled:tab==="Inspections"});
const faults=useQuery({queryKey:["faults",id],queryFn:()=>apiFetch<Fault[]>(`/api/v1/equipment/${id}/faults`),enabled:tab==="Faults"});
const parts=useQuery({queryKey:["equipment-parts-history",id],queryFn:()=>apiFetch<any[]>(`/api/v1/equipment/parts/equipment/${id}/history`),enabled:tab==="Parts"});
const downtime=useQuery({queryKey:["downtime",id],queryFn:()=>apiFetch<any[]>(`/api/v1/equipment/${id}/downtime`),enabled:tab==="Downtime"});

const status=useMutation({
mutationFn:(value:string)=>apiFetch(`/api/v1/equipment/${id}/status`,{method:"PATCH",body:JSON.stringify({status:value})}),
onSuccess:()=>queryClient.invalidateQueries({queryKey:["equipment",id]})
});

async function qr(){
const blob=await apiBlob(`/api/v1/equipment/${id}/qr`);
const url=URL.createObjectURL(blob);
const a=document.createElement("a");a.href=url;a.download=`${equipment.data?.assetCode}-qr.png`;a.click();URL.revokeObjectURL(url);
}

if(equipment.isLoading)return <AppShell><div className="animate-pulse text-slate-500">Loading equipment passport...</div></AppShell>;
if(!equipment.data)return <AppShell><div className="rounded-xl bg-red-50 p-5 text-red-700">Equipment not found.</div></AppShell>;

const e=equipment.data;

return <AppShell>
<div className="mb-6">
<div className="flex flex-col justify-between gap-5 xl:flex-row xl:items-end">
<div>
<div className="text-xs font-bold tracking-[0.2em] text-cyan-700">EQUIPMENT PASSPORT</div>
<div className="mt-2 flex flex-wrap items-center gap-3"><h1 className="text-3xl font-bold">{e.name}</h1><StatusBadge value={e.status}/><StatusBadge value={e.criticality}/></div>
<p className="mt-2 text-slate-500">{e.assetCode} · {e.serialNumber} · {e.manufacturer} {e.model}</p>
</div>
<div className="flex flex-wrap gap-2">
<Link href={`/equipment/${id}/edit`} className="inline-flex items-center gap-2 rounded-xl border border-slate-300 bg-white px-4 py-2.5 text-sm font-semibold"><Edit3 size={17}/>Edit</Link>
<button onClick={qr} className="inline-flex items-center gap-2 rounded-xl border border-slate-300 bg-white px-4 py-2.5 text-sm font-semibold"><QrCode size={17}/>QR code</button>
<Link href={`/maintenance/new?equipmentId=${id}`} className="inline-flex items-center gap-2 rounded-xl bg-slate-950 px-4 py-2.5 text-sm font-semibold text-white"><Plus size={17}/>Maintenance</Link>
</div>
</div>
</div>

<div className="overflow-x-auto border-b border-slate-200"><div className="flex min-w-max gap-1">{tabs.map(x=><button key={x} onClick={()=>setTab(x)} className={`border-b-2 px-4 py-3 text-sm font-medium ${tab===x?"border-cyan-600 text-cyan-700":"border-transparent text-slate-500"}`}>{x}</button>)}</div></div>

{tab==="Overview"&&<div className="mt-6 grid gap-6 xl:grid-cols-[1.4fr_.6fr]">
<section className="rounded-2xl border border-slate-200 bg-white p-6 shadow-sm">
<h2 className="font-semibold">Identity & lifecycle</h2>
<div className="mt-5 grid gap-5 sm:grid-cols-2">
{[
["Asset code",e.assetCode],["Serial number",e.serialNumber],["Type",e.equipmentType],["Manufacturer",e.manufacturer],["Model",e.model],
["Location",[e.site,e.building,e.area].filter(Boolean).join(" / ")||"Unassigned"],["Department",e.responsibleDepartment??"Unassigned"],["Warranty",e.warrantyExpiration??"Not recorded"]
].map(([k,v])=><div key={k}><div className="text-xs uppercase tracking-wide text-slate-400">{k}</div><div className="mt-1 font-medium">{v}</div></div>)}
</div>
<div className="mt-7 border-t pt-5"><div className="text-xs uppercase tracking-wide text-slate-400">Notes</div><p className="mt-2 whitespace-pre-wrap text-sm leading-7 text-slate-600">{e.notes||"No notes recorded."}</p></div>
</section>

<section className="space-y-6">
<div className="rounded-2xl border border-slate-200 bg-white p-6 shadow-sm">
<div className="flex items-center gap-2"><Wrench size={19}/><h2 className="font-semibold">Lifecycle status</h2></div>
<select value={e.status} onChange={x=>status.mutate(x.target.value)} className="mt-5 w-full rounded-xl border px-4 py-3"><option value="ACTIVE">Active</option><option value="UNDER_MAINTENANCE">Under maintenance</option><option value="OUT_OF_SERVICE">Out of service</option><option value="RETIRED">Retired</option><option value="DECOMMISSIONED">Decommissioned</option></select>
</div>
<div className="rounded-2xl bg-slate-950 p-6 text-white">
<div className="flex items-center gap-2"><QrCode className="text-cyan-400"/><h2 className="font-semibold">QR identification</h2></div>
<p className="mt-3 text-sm text-slate-300">Scanning the equipment code opens this passport.</p>
<div className="mt-4 rounded-xl bg-white/5 p-4 font-mono text-xs text-slate-400">iep://equipment/{e.id}</div>
</div>
</section>
</div>}

{tab==="Inspections"&&<section className="mt-6 rounded-2xl border border-slate-200 bg-white p-6 shadow-sm"><div className="flex items-center gap-2"><CalendarClock/><h2 className="font-semibold">Inspection history</h2></div><div className="mt-5 space-y-3">{inspections.data?.map(x=><div key={x.id} className="rounded-xl border p-4"><div className="flex justify-between gap-3"><div className="font-semibold">{x.inspectionDate}</div><StatusBadge value={x.result}/></div><p className="mt-2 text-sm text-slate-600">{x.checklist}</p>{x.notes&&<p className="mt-2 text-sm text-slate-500">{x.notes}</p>}</div>)}{!inspections.data?.length&&<div className="p-8 text-center text-slate-400">No inspections recorded.</div>}</div></section>}

{tab==="Parts"&&<section className="mt-6 rounded-2xl border border-slate-200 bg-white p-6 shadow-sm"><div className="flex items-center gap-2"><Package/><h2 className="font-semibold">Part replacement history</h2></div><div className="mt-5 space-y-3">{parts.data?.map(x=><div key={x.id} className="flex justify-between rounded-xl border p-4"><div><div className="font-medium">{x.part?.name??"Part"}</div><div className="text-xs text-slate-500">{x.part?.partNumber}</div></div><div className="text-right"><div className="font-semibold">×{x.quantity}</div><div className="text-xs text-slate-400">{new Date(x.replacedAt).toLocaleDateString()}</div></div></div>)}{!parts.data?.length&&<div className="p-8 text-center text-slate-400">No replacements recorded.</div>}</div></section>}

{tab==="Downtime"&&<section className="mt-6 rounded-2xl border border-slate-200 bg-white p-6 shadow-sm"><h2 className="font-semibold">Downtime history</h2><div className="mt-5 space-y-3">{downtime.data?.map(x=><div key={x.id} className="rounded-xl border p-4"><div className="flex justify-between"><span className="font-medium">{x.reason}</span><span className="text-sm text-slate-500">{x.durationMinutes} min</span></div><div className="mt-2 text-xs text-slate-400">{new Date(x.startTime).toLocaleString()} → {x.endTime?new Date(x.endTime).toLocaleString():"ongoing"}</div></div>)}{!downtime.data?.length&&<div className="p-8 text-center text-slate-400">No downtime records.</div>}</div></section>}

{tab==="Faults"&&<section className="mt-6 rounded-2xl border border-slate-200 bg-white p-6 shadow-sm"><div className="flex items-center gap-2"><AlertTriangle/><h2 className="font-semibold">Fault reports</h2></div><div className="mt-5 space-y-3">{faults.data?.map(x=><div key={x.id} className="rounded-xl border p-4"><div className="flex flex-wrap items-center justify-between gap-2"><div className="font-semibold">{x.title}</div><StatusBadge value={x.severity}/></div><p className="mt-2 text-sm text-slate-600">{x.description}</p><div className="mt-2 text-xs text-slate-400">{x.status} · {new Date(x.reportedAt).toLocaleString()}</div></div>)}{!faults.data?.length&&<div className="p-8 text-center text-slate-400">No fault reports.</div>}</div></section>}

{tab==="Attachments"&&<div className="mt-6"><AttachmentPanel equipmentId={id}/></div>}

{tab==="Maintenance"&&<div className="mt-6 rounded-2xl border border-slate-200 bg-white p-8 text-center"><Wrench className="mx-auto mb-3"/><p className="text-slate-500">Use the Maintenance tab from the main navigation to manage work orders.</p></div>}

{tab==="Audit History"&&<div className="mt-6 rounded-2xl border border-slate-200 bg-white p-8 text-center"><History className="mx-auto mb-3"/><p className="text-slate-500">The system-wide audit history is available from the Audit Log workspace.</p></div>}
</AppShell>;
}
