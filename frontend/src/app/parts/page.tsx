"use client";

import {useState} from "react";
import {useQuery,useMutation,useQueryClient} from "@tanstack/react-query";
import {PackageCheck,Plus} from "lucide-react";
import {AppShell} from "@/components/app-shell";
import {PageHeader} from "@/components/page-header";
import {apiFetch} from "@/lib/api";

type Part={id:string;partNumber:string;name:string;manufacturer?:string;quantity:number;minimumQuantity:number;unitCost:number;location?:string};

export default function PartsPage(){
const qc=useQueryClient();
const [open,setOpen]=useState(false);
const [form,setForm]=useState({partNumber:"",name:"",description:"",manufacturer:"",quantity:0,minimumQuantity:1,unitCost:0,location:""});
const query=useQuery({queryKey:["parts"],queryFn:()=>apiFetch<Part[]>("/api/v1/equipment/parts")});
const low=useQuery({queryKey:["parts-low"],queryFn:()=>apiFetch<Part[]>("/api/v1/equipment/parts/low-stock")});

const create=useMutation({
mutationFn:()=>apiFetch("/api/v1/equipment/parts",{method:"POST",body:JSON.stringify(form)}),
onSuccess:()=>{qc.invalidateQueries({queryKey:["parts"]});qc.invalidateQueries({queryKey:["parts-low"]});setOpen(false);}
});

return <AppShell>
<PageHeader eyebrow="INVENTORY" title="Spare parts" description="Inventory levels, minimum thresholds and maintenance stock." action={<button onClick={()=>setOpen(true)} className="inline-flex items-center gap-2 rounded-xl bg-slate-950 px-4 py-3 text-sm font-semibold text-white"><Plus size={18}/>Add part</button>}/>
<div className="mb-5 rounded-2xl border border-amber-200 bg-amber-50 p-5"><div className="font-semibold text-amber-900">Low-stock items</div><div className="mt-1 text-sm text-amber-800">{low.data?.length??0} parts currently need replenishment.</div></div>
<div className="overflow-hidden rounded-2xl border bg-white shadow-sm">
<div className="overflow-x-auto">
<table className="w-full min-w-[750px] text-left">
<thead className="border-b bg-slate-50 text-xs uppercase tracking-wide text-slate-500"><tr><th className="px-5 py-4">Part</th><th className="px-5 py-4">Manufacturer</th><th className="px-5 py-4">Quantity</th><th className="px-5 py-4">Minimum</th><th className="px-5 py-4">Cost</th><th className="px-5 py-4">Location</th></tr></thead>
<tbody className="divide-y">
{query.data?.map(p=><tr key={p.id}><td className="px-5 py-4"><div className="font-semibold">{p.name}</div><div className="text-xs text-slate-400">{p.partNumber}</div></td><td className="px-5 py-4 text-sm">{p.manufacturer??"—"}</td><td className="px-5 py-4"><span className={`rounded-full px-3 py-1 text-xs font-semibold ${p.quantity<=p.minimumQuantity?"bg-red-100 text-red-700":"bg-emerald-100 text-emerald-700"}`}>{p.quantity}</span></td><td className="px-5 py-4">{p.minimumQuantity}</td><td className="px-5 py-4">{Number(p.unitCost).toFixed(2)}</td><td className="px-5 py-4">{p.location??"—"}</td></tr>)}
</tbody>
</table>
</div>
</div>

{open&&<div className="fixed inset-0 z-50 flex items-center justify-center bg-slate-950/50 p-4"><div className="w-full max-w-lg rounded-2xl bg-white p-6 shadow-xl"><h2 className="text-xl font-semibold">Add spare part</h2><div className="mt-5 space-y-4">
{Object.entries(form).map(([key,value])=><div key={key}><label className="mb-1 block text-sm font-medium">{key}</label><input value={value as any} onChange={e=>setForm({...form,[key]:["quantity","minimumQuantity","unitCost"].includes(key)?Number(e.target.value):e.target.value})} type={["quantity","minimumQuantity","unitCost"].includes(key)?"number":"text"} step={key==="unitCost"?"0.01":"1"} className="w-full rounded-xl border px-4 py-3"/></div>)}
<div className="flex justify-end gap-2"><button onClick={()=>setOpen(false)} className="rounded-xl border px-4 py-2.5 font-semibold">Cancel</button><button onClick={()=>create.mutate()} className="rounded-xl bg-slate-950 px-4 py-2.5 font-semibold text-white">Save part</button></div>
</div></div></div>}
</AppShell>;
}
