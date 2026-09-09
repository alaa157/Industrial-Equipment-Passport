"use client";

import { useRouter } from "next/navigation";
import { useForm } from "react-hook-form";
import { useMutation } from "@tanstack/react-query";
import { z } from "zod";
import { zodResolver } from "@hookform/resolvers/zod";
import { AppShell } from "@/components/app-shell";
import { PageHeader } from "@/components/page-header";
import { apiFetch } from "@/lib/api";
import { useEquipmentTypes, useSites } from "@/lib/hooks";

const schema = z.object({
  assetCode: z.string().min(1).max(60),
  serialNumber: z.string().min(1).max(120),
  name: z.string().min(1).max(180),
  description: z.string().max(1000).optional(),
  manufacturer: z.string().min(1).max(150),
  model: z.string().min(1).max(150),
  equipmentTypeId: z.string().uuid(),
  purchaseDate: z.string().optional(),
  installationDate: z.string().optional(),
  warrantyExpiration: z.string().optional(),
  criticality: z.enum(["LOW", "MEDIUM", "HIGH", "CRITICAL"]),
  siteId: z.string().uuid().optional().or(z.literal("")),
  responsibleDepartment: z.string().max(120).optional(),
  notes: z.string().max(2000).optional(),
});

type Form = z.infer<typeof schema>;
const input =
  "w-full rounded-xl border border-slate-300 bg-white px-4 py-3 outline-none focus:border-cyan-600 focus:ring-2 focus:ring-cyan-100";

export default function NewEquipment() {
  const router = useRouter();
  const types = useEquipmentTypes();
  const sites = useSites();
  const form = useForm<Form>({
    resolver: zodResolver(schema),
    defaultValues: { criticality: "MEDIUM" },
  });
  const mutation = useMutation({
    mutationFn: (data: Form) =>
      apiFetch<any>("/api/v1/equipment", {
        method: "POST",
        body: JSON.stringify({
          ...data,
          siteId: data.siteId || null,
          purchaseDate: data.purchaseDate || null,
          installationDate: data.installationDate || null,
          warrantyExpiration: data.warrantyExpiration || null,
        }),
      }),
    onSuccess: (data) => router.push(`/equipment/${data.id}`),
  });

  return (
    <AppShell>
      <div className="mx-auto max-w-5xl">
        <PageHeader
          eyebrow="ASSET REGISTER"
          title="Register equipment"
          description="Create a complete industrial asset identity record."
        />
        <form
          onSubmit={form.handleSubmit((x) => mutation.mutate(x))}
          className="space-y-6"
        >
          <section className="rounded-2xl border border-slate-200 bg-white p-6 shadow-sm">
            <h2 className="font-semibold">Identification</h2>
            <div className="mt-5 grid gap-5 md:grid-cols-2">
              <div>
                <label className="mb-2 block text-sm font-medium">
                  Asset code
                </label>
                <input
                  {...form.register("assetCode")}
                  className={input}
                  placeholder="CNC-PRD-001"
                />
              </div>
              <div>
                <label className="mb-2 block text-sm font-medium">
                  Serial number
                </label>
                <input
                  {...form.register("serialNumber")}
                  className={input}
                  placeholder="DMG-SN-45821"
                />
              </div>
              <div>
                <label className="mb-2 block text-sm font-medium">
                  Equipment name
                </label>
                <input
                  {...form.register("name")}
                  className={input}
                  placeholder="5-Axis CNC Machining Center"
                />
              </div>
              <div>
                <label className="mb-2 block text-sm font-medium">
                  Equipment type
                </label>
                <select {...form.register("equipmentTypeId")} className={input}>
                  <option value="">Select type</option>
                  {types.data?.map((x) => (
                    <option key={x.id} value={x.id}>
                      {x.name}
                    </option>
                  ))}
                </select>
              </div>
              <div>
                <label className="mb-2 block text-sm font-medium">
                  Manufacturer
                </label>
                <input {...form.register("manufacturer")} className={input} />
              </div>
              <div>
                <label className="mb-2 block text-sm font-medium">Model</label>
                <input {...form.register("model")} className={input} />
              </div>
            </div>
            <div className="mt-5">
              <label className="mb-2 block text-sm font-medium">
                Description
              </label>
              <textarea
                {...form.register("description")}
                rows={4}
                className={input}
              />
            </div>
          </section>

          <section className="rounded-2xl border border-slate-200 bg-white p-6 shadow-sm">
            <h2 className="font-semibold">Lifecycle & location</h2>
            <div className="mt-5 grid gap-5 md:grid-cols-3">
              <div>
                <label className="mb-2 block text-sm font-medium">
                  Purchase
                </label>
                <input
                  type="date"
                  {...form.register("purchaseDate")}
                  className={input}
                />
              </div>
              <div>
                <label className="mb-2 block text-sm font-medium">
                  Installation
                </label>
                <input
                  type="date"
                  {...form.register("installationDate")}
                  className={input}
                />
              </div>
              <div>
                <label className="mb-2 block text-sm font-medium">
                  Warranty
                </label>
                <input
                  type="date"
                  {...form.register("warrantyExpiration")}
                  className={input}
                />
              </div>
              <div>
                <label className="mb-2 block text-sm font-medium">
                  Criticality
                </label>
                <select {...form.register("criticality")} className={input}>
                  <option value="LOW">Low</option>
                  <option value="MEDIUM">Medium</option>
                  <option value="HIGH">High</option>
                  <option value="CRITICAL">Critical</option>
                </select>
              </div>
              <div>
                <label className="mb-2 block text-sm font-medium">Site</label>
                <select {...form.register("siteId")} className={input}>
                  <option value="">Unassigned</option>
                  {sites.data?.map((x) => (
                    <option key={x.id} value={x.id}>
                      {x.name}
                    </option>
                  ))}
                </select>
              </div>
              <div>
                <label className="mb-2 block text-sm font-medium">
                  Department
                </label>
                <input
                  {...form.register("responsibleDepartment")}
                  className={input}
                />
              </div>
            </div>
          </section>

          <section className="rounded-2xl border border-slate-200 bg-white p-6 shadow-sm">
            <h2 className="font-semibold">Operational notes</h2>
            <textarea
              {...form.register("notes")}
              rows={5}
              className={`${input} mt-4`}
            />
          </section>

          {mutation.error && (
            <div className="rounded-xl border border-red-200 bg-red-50 p-4 text-sm text-red-700">
              {mutation.error.message}
            </div>
          )}
          <div className="flex justify-end gap-3">
            <button
              type="button"
              onClick={() => router.back()}
              className="rounded-xl border px-5 py-3 font-semibold"
            >
              Cancel
            </button>
            <button
              disabled={mutation.isPending}
              className="rounded-xl bg-slate-950 px-5 py-3 font-semibold text-white"
            >
              {mutation.isPending ? "Registering..." : "Register equipment"}
            </button>
          </div>
        </form>
      </div>
    </AppShell>
  );
}
