import {AppShell} from "@/components/app-shell";
import {PageHeader} from "@/components/page-header";

const sections=[
["Authentication","JWT access and refresh token policy, password hashing and backend authorization."],
["Storage","Files are retained in the local ./storage directory with server-generated filenames and validation."],
["Messaging","RabbitMQ domain events connect maintenance, notification, audit and equipment workflows."],
["Monitoring","Prometheus collects service metrics and Grafana provides the operations dashboard."],
["Database","PostgreSQL databases are isolated by service while maintaining relational integrity."],
["Security","Configuration comes from environment variables and secrets are intentionally excluded from source control."]
];

export default function Settings(){
return <AppShell>
<PageHeader eyebrow="SYSTEM" title="Settings" description="Platform architecture and runtime configuration."/>
<div className="grid gap-5 md:grid-cols-2">{sections.map(([title,text])=><section key={title} className="rounded-2xl border bg-white p-6 shadow-sm"><h2 className="font-semibold">{title}</h2><p className="mt-2 text-sm leading-7 text-slate-500">{text}</p></section>)}</div>
</AppShell>;
}
