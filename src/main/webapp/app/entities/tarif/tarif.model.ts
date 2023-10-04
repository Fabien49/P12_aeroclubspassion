import { IAeroclub } from 'app/entities/aeroclub/aeroclub.model';

export interface ITarif {
  id?: number;
  taxeAtterrissage?: number | null;
  taxeParking?: number | null;
  carburant?: number | null;
  aeroclub?: IAeroclub | null;
}

export class Tarif implements ITarif {
  constructor(
    public id?: number,
    public taxeAtterrissage?: number | null,
    public taxeParking?: number | null,
    public carburant?: number | null,
    public aeroclub?: IAeroclub | null
  ) {}
}

export function getTarifIdentifier(tarif: ITarif): number | undefined {
  return tarif.id;
}
