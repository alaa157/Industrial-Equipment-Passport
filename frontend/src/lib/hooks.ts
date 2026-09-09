"use client";

import { useQuery } from "@tanstack/react-query";
import { apiFetch } from "@/lib/api";
import { Equipment } from "@/lib/types";

export function useEquipment(id: string) {
  return useQuery({
    queryKey: ["equipment", id],
    queryFn: () => apiFetch<Equipment>(`/api/v1/equipment/${id}`),
    enabled: Boolean(id),
  });
}

export function useEquipmentTypes() {
  return useQuery({
    queryKey: ["equipment-types"],
    queryFn: () =>
      apiFetch<Array<{ id: string; name: string; description?: string }>>(
        "/api/v1/equipment/catalog/types",
      ),
  });
}

export function useSites() {
  return useQuery({
    queryKey: ["sites"],
    queryFn: () =>
      apiFetch<
        Array<{ id: string; code: string; name: string; address?: string }>
      >("/api/v1/equipment/catalog/sites"),
  });
}

export function useTechnicians() {
  return useQuery({
    queryKey: ["technicians"],
    queryFn: () =>
      apiFetch<
        Array<{
          id: string;
          userId: string;
          name: string;
          specialization?: string;
          phone?: string;
        }>
      >("/api/v1/technicians"),
  });
}

export function useBuildings(siteId?: string) {
  return useQuery({
    queryKey: ["buildings", siteId],
    queryFn: () =>
      apiFetch<
        Array<{
          id: string;
          code: string;
          name: string;
        }>
      >(
        `/api/v1/equipment/catalog/buildings?siteId=${encodeURIComponent(siteId!)}`,
      ),
    enabled: Boolean(siteId),
  });
}

export function useAreas(buildingId?: string) {
  return useQuery({
    queryKey: ["areas", buildingId],
    queryFn: () =>
      apiFetch<
        Array<{
          id: string;
          code: string;
          name: string;
        }>
      >(
        `/api/v1/equipment/catalog/areas?buildingId=${encodeURIComponent(buildingId!)}`,
      ),
    enabled: Boolean(buildingId),
  });
}
