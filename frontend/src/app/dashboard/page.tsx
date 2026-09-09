"use client";

import {useEffect} from "react";
import {useRouter} from "next/navigation";
import {useQuery} from "@tanstack/react-query";
import {Activity,Boxes,ClipboardList,Clock3,PackageCheck,TriangleAlert,Wrench} from "lucide-react";
import {PieChart,Pie,Cell,ResponsiveContainer,Tooltip,BarChart,Bar,XAxis,YAxis} from "recharts";
import {AppShell} from "@/components/app-shell";
import {StatCard} from "@/components/stat-card";
import {apiFetch} from "@/lib/api";
import {getAccessToken} from "@/lib/auth";

export default function Dashboard(){
const router=useRouter();
useEffect(()=>{if(!getAccessToken())router.replace("/login")},[router]);

const equipment=useQuery({queryKey:["equipment-dashboard"],queryFn:()=>apiFetch<any>("/api/v1/equipment/dashboard")});
const maintenance=useQuery({queryKey:["maintenance-dashboard"],queryFn:()=>apiFetch<any>("/api/v1/maintenance/dashboard")});
const parts=useQuery({queryKey:["parts-low"],queryFn:()=>apiFetch<any[]>("/api/v1/equipment/parts/low-stock")});

if(equipment.isLoading||maintenance.isLoading)return <AppShell><div className="animate-pulse text-slate-500">Loading plant intelligence...</div></AppShell>;

const statusData=[
{name:"Active",value:equipment.data?.activeEquipment??0},
{name:"Maintenance",value:equipment.data?.underMaintenance??0},
{name:"Out of service",value:equipment.data?.outOfService??0},
{name:"Retired",value:equipment.data?.retired??0}
].filter(x=>x.value>0);

const workload=[
{name:"Open",value:maintenance.data?.open??0},
{name:"Assigned",value:maintenance.data?.assigned??0},
{name:"In progress",value:maintenance.data?.inProgress??0},
{name:"Completed",value:maintenance.data?.completed??0}
];

return <AppShell>
<div className="mb-7">
<div className="text-xs font-bold tracking-[0.2em] text-cyan-700">PLANT OPERATIONS</div>
<h1 className="mt-2 text-3xl font-bold">Operations dashboard</h1>
<p className="mt-2 text-slate-500">Live visibility across assets, work orders and inventory.</p>
</div>

<div className="grid gap-4 sm:grid-cols-2 xl:grid-cols-4">
<StatCard label="Total equipment" value={equipment.data?.totalEquipment??0} icon={Boxes}/>
<StatCard label="Active" value={equipment.data?.activeEquipment??0} icon={Activity}/>
<StatCard label="Maintenance" value={equipment.data?.underMaintenance??0} icon={Wrench}/>
<StatCard label="Low-stock parts" value={parts.data?.length??0} icon={PackageCheck}/>
</div>

<div className="mt-6 grid gap-6 xl:grid-cols-2">
<section className="rounded-2xl border border-slate-200 bg-white p-6 shadow-sm">
<div className="flex items-center justify-between"><div><h2 className="font-semibold">Equipment status</h2><p className="text-sm text-slate-500">Current asset distribution</p></div><Boxes size={20} className="text-slate-400"/></div>
<div className="mt-5 h-72">
{statusData.length?<ResponsiveContainer width="100%" height="100%"><PieChart><Pie data={statusData} dataKey="value" nameKey="name" cx="50%" cy="50%" outerRadius={95} label>{statusData.map((_,i)=><Cell key={i}/>)}</Pie><Tooltip/></PieChart></ResponsiveContainer>:<div className="flex h-full items-center justify-center text-slate-400">No equipment data.</div>}
</div>
</section>

<section className="rounded-2xl border border-slate-200 bg-white p-6 shadow-sm">
<div className="flex items-center justify-between"><div><h2 className="font-semibold">Maintenance workload</h2><p className="text-sm text-slate-500">Current service pipeline</p></div><ClipboardList size={20} className="text-slate-400"/></div>
<div className="mt-5 h-72">
<ResponsiveContainer width="100%" height="100%">
<BarChart data={workload}><XAxis dataKey="name"/><YAxis allowDecimals={false}/><Tooltip/><Bar dataKey="value" radius={[6,6,0,0]}/></BarChart>
</ResponsiveContainer>
</div>
</section>
</div>

<div className="mt-6 grid gap-6 lg:grid-cols-3">
<div className="rounded-2xl border border-slate-200 bg-white p-6 shadow-sm">
<div className="flex items-center gap-3"><Clock3 className="text-slate-500"/><div><div className="text-sm text-slate-500">Overdue work</div><div className="mt-1 text-3xl font-bold">{maintenance.data?.overdue??0}</div></div></div>
</div>
<div className="rounded-2xl border border-slate-200 bg-white p-6 shadow-sm">
<div className="flex items-center gap-3"><TriangleAlert className="text-amber-500"/><div><div className="text-sm text-slate-500">Critical maintenance</div><div className="mt-1 text-3xl font-bold">{maintenance.data?.critical??0}</div></div></div>
</div>
<div className="rounded-2xl border border-slate-200 bg-slate-950 p-6 text-white shadow-sm">
<div className="flex items-center gap-3"><Activity className="text-cyan-400"/><div><div className="text-sm text-slate-400">System state</div><div className="mt-1 text-xl font-semibold">Operational</div></div></div>
</div>
</div>
</AppShell>;
}
