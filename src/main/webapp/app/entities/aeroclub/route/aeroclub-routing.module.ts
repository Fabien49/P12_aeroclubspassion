import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AeroclubComponent } from '../list/aeroclub.component';
import { AeroclubDetailComponent } from '../detail/aeroclub-detail.component';
import { AeroclubUpdateComponent } from '../update/aeroclub-update.component';
import { AeroclubRoutingResolveService } from './aeroclub-routing-resolve.service';

const aeroclubRoute: Routes = [
  {
    path: '',
    component: AeroclubComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AeroclubDetailComponent,
    resolve: {
      aeroclub: AeroclubRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AeroclubUpdateComponent,
    resolve: {
      aeroclub: AeroclubRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AeroclubUpdateComponent,
    resolve: {
      aeroclub: AeroclubRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(aeroclubRoute)],
  exports: [RouterModule],
})
export class AeroclubRoutingModule {}
