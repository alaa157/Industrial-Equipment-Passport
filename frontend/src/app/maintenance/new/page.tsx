"use client";

import {useSearchParams} from "next/navigation";
import {useRouter} from "next/navigation";
import {useForm} from "react-hook-form";
import { Suspense } from "react";
import {useMutation} from "@tanstack/react-query";
import {AppShell} from "@/components/app-shell";
import {PageHeader} from "@/components/page-header";
import {apiFetch} from "@/lib/api";

type Form={equipmentId:string;title:string;description:string;type:"PREVENTIVE"|"CORRECTIVE"|"SCHEDULED"|"EMERGENCY";priority:"LOW"|"MEDIUM"|"HIGH"|"CRITICAL";dueDate:string};

const input="w-full rounded-xl border border-slate-300 px-4 py-3 outline-none focus:border-cyan-600";

function MaintenanceForm() {
  const params=useSearchParams();
  const router=useRouter();
  const equipmentId=params.get("equipmentId")??"";
  const form=useForm<Form>({defaultValues:{equipmentId,type:"CORRECTIVE",priority:"MEDIUM",title:"",description:"",dueDate:""}});

  const mutation=useMutation({
    mutationFn:(data:Form)=>apiFetch("/api/v1/maintenance",{method:"POST",body:JSON.stringify({...data,dueDate:data.dueDate||null})}),
    onSuccess:()=>router.push("/maintenance")
  });

  return (
    <form onSubmit={form.handleSubmit(v=>mutation.mutate(v))} className="rounded-2xl border bg-white p-6 shadow-sm">
      <div><label className="mb-2 block text-sm font-medium">Equipment ID</label><input {...form.register("equipmentId",{required:true})} className={input}/></div>
      <div className="mt-5"><label className="mb-2 block text-sm font-medium">Title</label><input {...form.register("title",{required:true})} className={input}/></div>
      <div className="mt-5"><label className="mb-2 block text-sm font-medium">Description</label><textarea rows={6} {...form.register("description",{required:true})} className={input}/></div>
      <div className="mt-5 grid gap-4 sm:grid-cols-3">
        <div><label className="mb-2 block text-sm font-medium">Type</label><select {...form.register("type")} className={input}><option value="PREVENTIVE">Preventive</option><option value="CORRECTIVE">Corrective</option><option value="SCHEDULED">Scheduled</option><option value="EMERGENCY">Emergency</option></select></div>
        <div><label className="mb-2 block text-sm font-medium">Priority</label><select {...form.register("priority")} className={input}><option value="LOW">Low</option><option value="MEDIUM">Medium</option><option value="HIGH">High</option><option value="CRITICAL">Critical</option></select></div>
        <div><label className="mb-2 block text-sm font-medium">Due date</label><input type="date" {...form.register("dueDate")} className={input}/></div>
      </div>
      {mutation.error&&<div className="mt-5 rounded-xl bg-red-50 p-4 text-sm text-red-700">{mutation.error.message}</div>}
      <div className="mt-6 flex justify-end gap-3"><button type="button" onClick={()=>router.back()} className="rounded-xl border px-5 py-3 font-semibold">Cancel</button><button disabled={mutation.isPending} className="rounded-xl bg-slate-950 px-5 py-3 font-semibold text-white">{mutation.isPending?"Creating...":"Create request"}</button></div>
    </form>
  );
}

export default function NewMaintenance(){
  return (
    <AppShell>
      <div className="mx-auto max-w-3xl">
        <PageHeader eyebrow="SERVICE REQUEST" title="Create maintenance request" description="Open a new work order for an industrial asset."/>
        <Suspense fallback={<div className="p-6 text-center text-slate-500">Loading form...</div>}>
          <MaintenanceForm />
        </Suspense>
      </div>
    </AppShell>
  );
}