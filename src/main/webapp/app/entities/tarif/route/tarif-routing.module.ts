import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TarifComponent } from '../list/tarif.component';
import { TarifDetailComponent } from '../detail/tarif-detail.component';
import { TarifUpdateComponent } from '../update/tarif-update.component';
import { TarifRoutingResolveService } from './tarif-routing-resolve.service';

const tarifRoute: Routes = [
  {
    path: '',
    component: TarifComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TarifDetailComponent,
    resolve: {
      tarif: TarifRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TarifUpdateComponent,
    resolve: {
      tarif: TarifRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TarifUpdateComponent,
    resolve: {
      tarif: TarifRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(tarifRoute)],
  exports: [RouterModule],
})
export class TarifRoutingModule {}
