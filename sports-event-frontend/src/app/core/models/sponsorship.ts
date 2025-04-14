import { Sponsor } from "./sponsor";

export interface Sponsorship {
    id: number;
    sponsor: Sponsor;
    event: Event;
    contributionAmount: number;
    status: 'PENDING' | 'ACCEPTED' | 'REJECTED';
    requestDate: string;
    decisionDate?: string;
    notes?: string;
  }