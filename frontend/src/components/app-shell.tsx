"use client";

import Link from "next/link";
import { usePathname, useRouter } from "next/navigation";
import { useMemo, useState } from "react";
import {
  Activity,
  Boxes,
  ClipboardCheck,
  ClipboardList,
  LayoutDashboard,
  LogOut,
  Menu,
  PackageCheck,
  Settings,
  ShieldCheck,
  UserRoundCog,
  Users,
  X,
} from "lucide-react";
import { clearAuth } from "@/lib/auth";
import { getCurrentUser } from "@/lib/current-user";

const links = [
  { href: "/dashboard", label: "Dashboard", icon: LayoutDashboard },
  { href: "/equipment", label: "Equipment", icon: Boxes },
  { href: "/maintenance", label: "Maintenance", icon: ClipboardList },
  { href: "/inspections", label: "Inspections", icon: ClipboardCheck },
  { href: "/technicians", label: "Technicians", icon: UserRoundCog },
  { href: "/parts", label: "Spare Parts", icon: PackageCheck },
  { href: "/notifications", label: "Notifications", icon: Activity },
  { href: "/users", label: "Users", icon: Users },
  { href: "/roles", label: "Roles & Permissions", icon: ShieldCheck },
  { href: "/audit", label: "Audit Log", icon: ShieldCheck },
  { href: "/settings", label: "Settings", icon: Settings },
];

const rolePriority = [
  "ADMIN",
  "MANAGER",
  "TECHNICIAN",
  "INSPECTOR",
  "VIEWER",
];

export function AppShell({ children }: { children: React.ReactNode }) {
  const path = usePathname();
  const router = useRouter();
  const [mobileOpen, setMobileOpen] = useState(false);

  const currentUser = getCurrentUser();
  const isAdmin = currentUser?.roles.includes("ADMIN") ?? false;

  const visibleLinks = useMemo(
    () =>
      links.filter(
        (item) =>
          !["/users", "/roles"].includes(item.href) || isAdmin,
      ),
    [isAdmin],
  );

  const displayRole =
    rolePriority.find((role) => currentUser?.roles.includes(role)) ?? "USER";

  const username = currentUser?.username ?? "User";
  const initials = username.charAt(0).toUpperCase();

  function logout() {
    clearAuth();
    router.replace("/login");
  }

  return (
    <div className="min-h-screen bg-slate-100">
      <aside
        className={`fixed inset-y-0 left-0 z-50 w-72 border-r border-slate-800 bg-slate-950 text-white transition-transform lg:translate-x-0 ${
          mobileOpen ? "translate-x-0" : "-translate-x-full"
        }`}
      >
        <div className="flex h-20 items-center justify-between border-b border-white/10 px-6">
          <div>
            <div className="text-[10px] font-bold tracking-[0.2em] text-cyan-400">
              INDUSTRIAL EQUIPMENT
            </div>
            <div className="mt-1 text-lg font-bold">PASSPORT</div>
          </div>

          <button
            onClick={() => setMobileOpen(false)}
            className="lg:hidden"
            aria-label="Close menu"
          >
            <X />
          </button>
        </div>

        <nav className="space-y-1 p-4">
          {visibleLinks.map((item) => {
            const Icon = item.icon;
            const active =
              path === item.href || path.startsWith(item.href + "/");

            return (
              <Link
                key={item.href}
                href={item.href}
                onClick={() => setMobileOpen(false)}
                className={`flex items-center gap-3 rounded-xl px-4 py-3 text-sm font-medium ${
                  active
                    ? "bg-cyan-500/15 text-cyan-300"
                    : "text-slate-300 hover:bg-white/5 hover:text-white"
                }`}
              >
                <Icon size={18} />
                {item.label}
              </Link>
            );
          })}
        </nav>

        <button
          onClick={logout}
          className="absolute bottom-6 left-4 right-4 flex items-center gap-3 rounded-xl px-4 py-3 text-sm text-slate-400 hover:bg-white/5 hover:text-white"
        >
          <LogOut size={18} />
          Sign out
        </button>
      </aside>

      <div className="lg:pl-72">
        <header className="sticky top-0 z-30 flex h-20 items-center justify-between border-b border-slate-200 bg-white/90 px-5 backdrop-blur">
          <button
            onClick={() => setMobileOpen(true)}
            className="rounded-lg p-2 hover:bg-slate-100 lg:hidden"
            aria-label="Open menu"
          >
            <Menu />
          </button>

          <div className="hidden lg:block">
            <div className="text-xs font-semibold uppercase tracking-wider text-slate-400">
              Operations workspace
            </div>
            <div className="font-semibold">
              {visibleLinks.find(
                (x) => path === x.href || path.startsWith(x.href + "/"),
              )?.label ?? "Workspace"}
            </div>
          </div>

          <div className="flex items-center gap-3">
            <div className="flex h-9 w-9 items-center justify-center rounded-full bg-slate-900 text-sm font-bold text-white">
              {initials}
            </div>

            <div className="hidden sm:block">
              <div className="text-sm font-semibold">{username}</div>
              <div className="text-xs text-slate-500">{displayRole}</div>
            </div>
          </div>
        </header>

        <main className="min-h-[calc(100vh-80px)] p-4 sm:p-6 lg:p-8">
          {children}
        </main>
      </div>
    </div>
  );
}