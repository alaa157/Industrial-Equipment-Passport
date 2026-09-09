"use client";

import {useQuery} from "@tanstack/react-query";
import {UserRoundCog} from "lucide-react";
import {AppShell} from "@/components/app-shell";
import {PageHeader} from "@/components/page-header";
import {apiFetch} from "@/lib/api";

export default function Technicians(){
const query=useQuery({queryKey:["technicians"],queryFn:()=>apiFetch<any[]>("/api/v1/technicians")});

return <AppShell>
<PageHeader eyebrow="WORKFORCE" title="Technicians" description="Maintenance personnel and technical specializations."/>
<div className="grid gap-4 md:grid-cols-2 xl:grid-cols-3">
{query.data?.map(t=><div key={t.id} className="rounded-2xl border border-slate-200 bg-white p-6 shadow-sm">
<div className="flex items-center gap-3"><div className="rounded-full bg-slate-100 p-3"><UserRoundCog/></div><div><div className="font-semibold">{t.name}</div><div className="text-sm text-slate-500">{t.specialization??"General maintenance"}</div></div></div>
<div className="mt-5 border-t pt-4 text-sm text-slate-500">{t.phone??"No phone recorded"}</div>
</div>)}
</div>
</AppShell>;
}
