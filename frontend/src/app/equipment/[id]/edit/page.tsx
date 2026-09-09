"use client";

import {useParams,useRouter} from "next/navigation";
import {useForm} from "react-hook-form";
import {useEffect} from "react";
import {useMutation,useQueryClient} from "@tanstack/react-query";
import {AppShell} from "@/components/app-shell";
import {PageHeader} from "@/components/page-header";
import {apiFetch} from "@/lib/api";
import {useEquipment} from "@/lib/hooks";

type Form={
name:string;
description:string;
manufacturer:string;
model:string;
warrantyExpiration:string;
criticality:"LOW"|"MEDIUM"|"HIGH"|"CRITICAL";
responsibleDepartment:string;
notes:string;
};

const input="w-full rounded-xl border border-slate-300 bg-white px-4 py-3 outline-none focus:border-cyan-600";

export default function EditEquipment(){
const params=useParams<{id:string}>();
const router=useRouter();
const qc=useQueryClient();
const equipment=useEquipment(params.id);

const form=useForm<Form>();

useEffect(()=>{
if(equipment.data){
form.reset({
name:equipment.data.name,
description:equipment.data.description??"",
manufacturer:equipment.data.manufacturer,
model:equipment.data.model,
warrantyExpiration:equipment.data.warrantyExpiration??"",
criticality:equipment.data.criticality,
responsibleDepartment:equipment.data.responsibleDepartment??"",
notes:equipment.data.notes??""
});
}
},[equipment.data]);

const mutation=useMutation({
mutationFn:(data:Form)=>apiFetch(`/api/v1/equipment/${params.id}`,{
method:"PATCH",
body:JSON.stringify({...data,warrantyExpiration:data.warrantyExpiration||null})
}),
onSuccess:async()=>{
await qc.invalidateQueries({queryKey:["equipment",params.id]});
router.push(`/equipment/${params.id}`);
}
});

if(equipment.isLoading)return <AppShell><div>Loading...</div></AppShell>;

return <AppShell>
<div className="mx-auto max-w-4xl">
<PageHeader eyebrow="EQUIPMENT PASSPORT" title="Edit equipment" description={`${equipment.data?.assetCode??""} · ${equipment.data?.serialNumber??""}`}/>
<form onSubmit={form.handleSubmit(v=>mutation.mutate(v))} className="rounded-2xl border border-slate-200 bg-white p-6 shadow-sm">
<div className="grid gap-5 md:grid-cols-2">
<div><label className="mb-2 block text-sm font-medium">Name</label><input {...form.register("name")} className={input}/></div>
<div><label className="mb-2 block text-sm font-medium">Manufacturer</label><input {...form.register("manufacturer")} className={input}/></div>
<div><label className="mb-2 block text-sm font-medium">Model</label><input {...form.register("model")} className={input}/></div>
<div><label className="mb-2 block text-sm font-medium">Warranty expiration</label><input type="date" {...form.register("warrantyExpiration")} className={input}/></div>
<div><label className="mb-2 block text-sm font-medium">Criticality</label><select {...form.register("criticality")} className={input}><option value="LOW">Low</option><option value="MEDIUM">Medium</option><option value="HIGH">High</option><option value="CRITICAL">Critical</option></select></div>
<div><label className="mb-2 block text-sm font-medium">Department</label><input {...form.register("responsibleDepartment")} className={input}/></div>
</div>
<div className="mt-5"><label className="mb-2 block text-sm font-medium">Description</label><textarea rows={5} {...form.register("description")} className={input}/></div>
<div className="mt-5"><label className="mb-2 block text-sm font-medium">Notes</label><textarea rows={6} {...form.register("notes")} className={input}/></div>
{mutation.error&&<div className="mt-5 rounded-xl bg-red-50 p-4 text-sm text-red-700">{mutation.error.message}</div>}
<div className="mt-6 flex justify-end gap-3"><button type="button" onClick={()=>router.back()} className="rounded-xl border px-5 py-3 font-semibold">Cancel</button><button disabled={mutation.isPending} className="rounded-xl bg-slate-950 px-5 py-3 font-semibold text-white">{mutation.isPending?"Saving...":"Save changes"}</button></div>
</form>
</div>
</AppShell>;
}
