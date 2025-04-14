export interface Sponsor {
    id?: number;
    name: string;
    companyName: string;
    email: string;
    phone?: string;
    industry?: string;
    status?: 'ACTIVE' | 'INACTIVE';
  }