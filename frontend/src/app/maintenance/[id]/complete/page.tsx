"use client";

import {useParams,useRouter} from "next/navigation";
import {useForm} from "react-hook-form";
import {useMutation,useQueryClient} from "@tanstack/react-query";
import {AppShell} from "@/components/app-shell";
import {PageHeader} from "@/components/page-header";
import {apiFetch} from "@/lib/api";

type Form={actualDurationMinutes:number;maintenanceCost:number;laborNotes:string;completionNotes:string};

export default function CompleteMaintenance(){
const {id}=useParams<{id:string}>();
const router=useRouter();
const qc=useQueryClient();
const form=useForm<Form>({defaultValues:{actualDurationMinutes:60,maintenanceCost:0,laborNotes:"",completionNotes:""}});

const mutation=useMutation({
mutationFn:(data:Form)=>apiFetch(`/api/v1/maintenance/${id}/complete`,{method:"POST",body:JSON.stringify(data)}),
onSuccess:async()=>{
await qc.invalidateQueries({queryKey:["maintenance",id]});
router.push(`/maintenance/${id}`);
}
});

const input="w-full rounded-xl border border-slate-300 px-4 py-3 outline-none focus:border-cyan-600";

return <AppShell>
<div className="mx-auto max-w-3xl">
<PageHeader eyebrow="SERVICE MANAGEMENT" title="Complete maintenance" description="Record the actual work performed and close the work order."/>
<form onSubmit={form.handleSubmit(v=>mutation.mutate(v))} className="rounded-2xl border bg-white p-6 shadow-sm">
<div className="grid gap-5 sm:grid-cols-2">
<div><label className="mb-2 block text-sm font-medium">Actual duration (minutes)</label><input type="number" min="1" {...form.register("actualDurationMinutes",{valueAsNumber:true})} className={input}/></div>
<div><label className="mb-2 block text-sm font-medium">Maintenance cost</label><input type="number" min="0" step="0.01" {...form.register("maintenanceCost",{valueAsNumber:true})} className={input}/></div>
</div>
<div className="mt-5"><label className="mb-2 block text-sm font-medium">Labor notes</label><textarea rows={6} {...form.register("laborNotes")} className={input}/></div>
<div className="mt-5"><label className="mb-2 block text-sm font-medium">Completion notes</label><textarea rows={6} {...form.register("completionNotes")} className={input}/></div>
{mutation.error&&<div className="mt-5 rounded-xl bg-red-50 p-4 text-sm text-red-700">{mutation.error.message}</div>}
<div className="mt-6 flex justify-end gap-3"><button type="button" onClick={()=>router.back()} className="rounded-xl border px-5 py-3 font-semibold">Cancel</button><button disabled={mutation.isPending} className="rounded-xl bg-emerald-700 px-5 py-3 font-semibold text-white">{mutation.isPending?"Completing...":"Complete maintenance"}</button></div>
</form>
</div>
</AppShell>;
}
