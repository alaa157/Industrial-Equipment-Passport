export function StatCard({label,value,detail,icon:Icon}:{label:string;value:number|string;detail?:string;icon:React.ElementType}){
return <div className="rounded-2xl border border-slate-200 bg-white p-5 shadow-sm">
<div className="flex items-start justify-between">
<div>
<div className="text-sm text-slate-500">{label}</div>
<div className="mt-2 text-3xl font-bold">{value}</div>
{detail&&<div className="mt-1 text-xs text-slate-400">{detail}</div>}
</div>
<div className="rounded-xl bg-slate-100 p-3 text-slate-700"><Icon size={20}/></div>
</div>
</div>;
}
