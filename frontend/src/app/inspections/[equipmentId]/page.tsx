"use client";

import {useParams,useRouter} from "next/navigation";
import {useForm} from "react-hook-form";
import {useMutation,useQuery,useQueryClient} from "@tanstack/react-query";
import {AppShell} from "@/components/app-shell";
import {PageHeader} from "@/components/page-header";
import {StatusBadge} from "@/components/status-badge";
import {apiFetch} from "@/lib/api";

type Form={inspectionDate:string;checklist:string;result:"PASSED"|"PASSED_WITH_WARNINGS"|"FAILED";notes:string;nextInspectionDate:string};

export default function InspectionPage(){
const {equipmentId}=useParams<{equipmentId:string}>();
const router=useRouter();
const qc=useQueryClient();
const form=useForm<Form>({defaultValues:{inspectionDate:new Date().toISOString().slice(0,10),result:"PASSED",checklist:"",notes:"",nextInspectionDate:""}});
const equipment=useQuery({queryKey:["equipment",equipmentId],queryFn:()=>apiFetch<any>(`/api/v1/equipment/${equipmentId}`)});
const inspections=useQuery({queryKey:["inspections",equipmentId],queryFn:()=>apiFetch<any[]>(`/api/v1/equipment/${equipmentId}/inspections`)});

const create=useMutation({
mutationFn:(data:Form)=>apiFetch(`/api/v1/equipment/${equipmentId}/inspections`,{method:"POST",body:JSON.stringify({...data,nextInspectionDate:data.nextInspectionDate||null})}),
onSuccess:()=>{qc.invalidateQueries({queryKey:["inspections",equipmentId]});form.reset({...form.getValues(),checklist:"",notes:"",result:"PASSED"});}
});

const input="w-full rounded-xl border px-4 py-3 outline-none focus:border-cyan-600";

return <AppShell>
<PageHeader eyebrow="COMPLIANCE" title={equipment.data?.name??"Inspection"} description={equipment.data?.assetCode}/>
<div className="grid gap-6 xl:grid-cols-[1fr_1fr]">
<section className="rounded-2xl border bg-white p-6 shadow-sm">
<h2 className="font-semibold">Perform inspection</h2>
<form onSubmit={form.handleSubmit(v=>create.mutate(v))} className="mt-5 space-y-5">
<div><label className="mb-2 block text-sm font-medium">Inspection date</label><input type="date" {...form.register("inspectionDate")} className={input}/></div>
<div><label className="mb-2 block text-sm font-medium">Checklist / findings</label><textarea rows={8} {...form.register("checklist",{required:true})} className={input} placeholder="Guarding, lubrication, emergency stop, electrical cabinet, vibration..."/></div>
<div><label className="mb-2 block text-sm font-medium">Result</label><select {...form.register("result")} className={input}><option value="PASSED">Passed</option><option value="PASSED_WITH_WARNINGS">Passed with warnings</option><option value="FAILED">Failed</option></select></div>
<div><label className="mb-2 block text-sm font-medium">Notes</label><textarea rows={4} {...form.register("notes")} className={input}/></div>
<div><label className="mb-2 block text-sm font-medium">Next inspection</label><input type="date" {...form.register("nextInspectionDate")} className={input}/></div>
<button disabled={create.isPending} className="w-full rounded-xl bg-slate-950 px-4 py-3 font-semibold text-white">{create.isPending?"Saving...":"Record inspection"}</button>
</form>
</section>

<section className="rounded-2xl border bg-white p-6 shadow-sm">
<h2 className="font-semibold">History</h2>
<div className="mt-5 space-y-3">
{inspections.data?.map((x:any)=><div key={x.id} className="rounded-xl border p-4">
<div className="flex justify-between"><span className="font-semibold">{x.inspectionDate}</span><StatusBadge value={x.result}/></div>
<p className="mt-2 text-sm text-slate-600">{x.checklist}</p>
</div>)}
{!inspections.data?.length&&<div className="p-8 text-center text-slate-400">No previous inspections.</div>}
</div>
</section>
</div>
</AppShell>;
}
