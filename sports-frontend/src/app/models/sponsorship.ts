import { Sponsor } from './sponsor';
import { Event } from './event';

export interface Sponsorship {
  id?: number;
  sponsor: Sponsor;
  event: Event;
  contributionAmount: number;
  status: string;
  requestDate?: string;
}