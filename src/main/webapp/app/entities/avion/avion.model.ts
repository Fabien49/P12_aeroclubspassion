import { IAeroclub } from 'app/entities/aeroclub/aeroclub.model';

export interface IAvion {
  id?: number;
  marque?: string;
  type?: string | null;
  moteur?: string | null;
  puissance?: number | null;
  place?: number | null;
  autonomie?: string | null;
  usage?: string | null;
  heures?: string | null;
  image?: string | null;
  aeroclub?: IAeroclub | null;
}

export class Avion implements IAvion {
  constructor(
    public id?: number,
    public marque?: string,
    public type?: string | null,
    public moteur?: string | null,
    public puissance?: number | null,
    public place?: number | null,
    public autonomie?: string | null,
    public usage?: string | null,
    public heures?: string | null,
    public image?: string | null,
    public aeroclub?: IAeroclub | null
  ) {}
}

export function getAvionIdentifier(avion: IAvion): number | undefined {
  return avion.id;
}
