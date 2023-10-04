import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RevisionComponent } from '../list/revision.component';
import { RevisionDetailComponent } from '../detail/revision-detail.component';
import { RevisionUpdateComponent } from '../update/revision-update.component';
import { RevisionRoutingResolveService } from './revision-routing-resolve.service';

const revisionRoute: Routes = [
  {
    path: '',
    component: RevisionComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RevisionDetailComponent,
    resolve: {
      revision: RevisionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RevisionUpdateComponent,
    resolve: {
      revision: RevisionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RevisionUpdateComponent,
    resolve: {
      revision: RevisionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(revisionRoute)],
  exports: [RouterModule],
})
export class RevisionRoutingModule {}
