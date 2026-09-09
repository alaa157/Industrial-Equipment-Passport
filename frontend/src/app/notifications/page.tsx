"use client";

import {useQuery,useMutation,useQueryClient} from "@tanstack/react-query";
import {Bell,Check} from "lucide-react";
import {AppShell} from "@/components/app-shell";
import {PageHeader} from "@/components/page-header";
import {apiFetch} from "@/lib/api";

type Notification={id:string;title:string;message:string;eventType:string;createdAt:string;readAt?:string};

export default function Notifications(){
const qc=useQueryClient();
const query=useQuery({queryKey:["notifications"],queryFn:()=>apiFetch<Notification[]>("/api/v1/notifications")});
const read=useMutation({mutationFn:(id:string)=>apiFetch(`/api/v1/notifications/${id}/read`,{method:"POST"}),onSuccess:()=>qc.invalidateQueries({queryKey:["notifications"]})});

return <AppShell>
<PageHeader eyebrow="ALERT CENTER" title="Notifications" description="Assignments, inspection failures, inventory alerts and other operational events."/>
<div className="space-y-3">
{query.data?.map(n=><div key={n.id} className={`rounded-2xl border p-5 shadow-sm ${n.readAt?"bg-white border-slate-200":"bg-cyan-50/50 border-cyan-200"}`}><div className="flex items-start gap-4"><div className="rounded-xl bg-white p-3"><Bell size={18}/></div><div className="min-w-0 flex-1"><div className="flex flex-wrap justify-between gap-2"><h2 className="font-semibold">{n.title}</h2><span className="text-xs text-slate-400">{new Date(n.createdAt).toLocaleString()}</span></div><p className="mt-2 text-sm text-slate-600">{n.message}</p><div className="mt-2 text-xs font-semibold text-slate-400">{n.eventType}</div></div>{!n.readAt&&<button onClick={()=>read.mutate(n.id)} className="rounded-lg border bg-white p-2"><Check size={17}/></button>}</div></div>)}
{!query.data?.length&&!query.isLoading&&<div className="rounded-2xl border border-dashed p-12 text-center text-slate-400">No notifications.</div>}
</div>
</AppShell>;
}
