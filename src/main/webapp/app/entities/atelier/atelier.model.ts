import * as dayjs from 'dayjs';
import { IAvion } from 'app/entities/avion/avion.model';

export interface IAtelier {
  id?: number;
  compteurChgtMoteur?: number | null;
  compteurCarrosserie?: number | null;
  compteurHelisse?: number | null;
  date?: dayjs.Dayjs | null;
  avion?: IAvion | null;
}

export class Atelier implements IAtelier {
  constructor(
    public id?: number,
    public compteurChgtMoteur?: number | null,
    public compteurCarrosserie?: number | null,
    public compteurHelisse?: number | null,
    public date?: dayjs.Dayjs | null,
    public avion?: IAvion | null
  ) {}
}

export function getAtelierIdentifier(atelier: IAtelier): number | undefined {
  return atelier.id;
}
