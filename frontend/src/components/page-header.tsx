export function PageHeader({eyebrow,title,description,action}:{eyebrow:string;title:string;description?:string;action?:React.ReactNode}){
return <div className="mb-7 flex flex-col justify-between gap-4 md:flex-row md:items-end">
<div>
<div className="text-xs font-bold tracking-[0.2em] text-cyan-700">{eyebrow}</div>
<h1 className="mt-2 text-3xl font-bold tracking-tight">{title}</h1>
{description&&<p className="mt-2 text-slate-500">{description}</p>}
</div>
{action}
</div>;
}
