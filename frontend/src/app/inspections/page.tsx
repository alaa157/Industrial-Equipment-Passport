"use client";

import Link from "next/link";
import {useQuery} from "@tanstack/react-query";
import {ClipboardCheck} from "lucide-react";
import {AppShell} from "@/components/app-shell";
import {PageHeader} from "@/components/page-header";
import {apiFetch} from "@/lib/api";

export default function Inspections(){
const equipment=useQuery({queryKey:["inspection-equipment"],queryFn:()=>apiFetch<any>("/api/v1/equipment?size=100")});

return <AppShell>
<PageHeader eyebrow="COMPLIANCE" title="Inspections" description="Select an equipment passport to perform and review inspections."/>
<div className="grid gap-4 md:grid-cols-2 xl:grid-cols-3">
{equipment.data?.content?.map((e:any)=><Link key={e.id} href={`/inspections/${e.id}`} className="rounded-2xl border border-slate-200 bg-white p-5 shadow-sm hover:border-cyan-300 hover:shadow-md"><div className="flex items-center gap-3"><div className="rounded-xl bg-slate-100 p-3"><ClipboardCheck size={19}/></div><div><div className="font-semibold">{e.name}</div><div className="text-xs text-slate-400">{e.assetCode}</div></div></div></Link>)}
{!equipment.data?.content?.length&&!equipment.isLoading&&<div className="rounded-2xl border border-dashed p-8 text-slate-400">No equipment available.</div>}
</div>
</AppShell>;
}
