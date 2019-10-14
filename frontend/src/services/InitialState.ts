interface Router {
    page: string;
    updatePage(page: String): void;
}
interface AppInitialData extends Router {
    cars: {
      make: string;
      model: string;
      maturityDate: number;
      price: number;
    }[];
    username: string;
    edit: boolean;
    auth: boolean;
  }
  