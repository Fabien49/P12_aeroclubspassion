import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'aeroclub',
        data: { pageTitle: 'aeroclubspassionApp.aeroclub.home.title' },
        loadChildren: () => import('./aeroclub/aeroclub.module').then(m => m.AeroclubModule),
      },
      {
        path: 'tarif',
        data: { pageTitle: 'aeroclubspassionApp.tarif.home.title' },
        loadChildren: () => import('./tarif/tarif.module').then(m => m.TarifModule),
      },
      {
        path: 'avion',
        data: { pageTitle: 'aeroclubspassionApp.avion.home.title' },
        loadChildren: () => import('./avion/avion.module').then(m => m.AvionModule),
      },
      {
        path: 'reservation',
        data: { pageTitle: 'aeroclubspassionApp.reservation.home.title' },
        loadChildren: () => import('./reservation/reservation.module').then(m => m.ReservationModule),
      },
      {
        path: 'revision',
        data: { pageTitle: 'aeroclubspassionApp.revision.home.title' },
        loadChildren: () => import('./revision/revision.module').then(m => m.RevisionModule),
      },
      {
        path: 'atelier',
        data: { pageTitle: 'aeroclubspassionApp.atelier.home.title' },
        loadChildren: () => import('./atelier/atelier.module').then(m => m.AtelierModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
