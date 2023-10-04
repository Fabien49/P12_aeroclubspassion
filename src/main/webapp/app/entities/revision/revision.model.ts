import * as dayjs from 'dayjs';
import { IAvion } from 'app/entities/avion/avion.model';

export interface IRevision {
  id?: number;
  niveaux?: boolean | null;
  pression?: boolean | null;
  carroserie?: boolean | null;
  date?: dayjs.Dayjs | null;
  avion?: IAvion | null;
}

export class Revision implements IRevision {
  constructor(
    public id?: number,
    public niveaux?: boolean | null,
    public pression?: boolean | null,
    public carroserie?: boolean | null,
    public date?: dayjs.Dayjs | null,
    public avion?: IAvion | null
  ) {
    this.niveaux = this.niveaux ?? false;
    this.pression = this.pression ?? false;
    this.carroserie = this.carroserie ?? false;
  }
}

export function getRevisionIdentifier(revision: IRevision): number | undefined {
  return revision.id;
}
