import * as dayjs from 'dayjs';
import { IAvion } from 'app/entities/avion/avion.model';

export interface IReservation {
  id?: number;
  dateEmprunt?: dayjs.Dayjs | null;
  dateRetour?: dayjs.Dayjs | null;
  avion?: IAvion | null;
}

export class Reservation implements IReservation {
  constructor(
    public id?: number,
    public dateEmprunt?: dayjs.Dayjs | null,
    public dateRetour?: dayjs.Dayjs | null,
    public avion?: IAvion | null
  ) {}
}

export function getReservationIdentifier(reservation: IReservation): number | undefined {
  return reservation.id;
}
