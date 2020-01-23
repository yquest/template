export namespace CarMaker {
  export enum e {
    AUDI,
    VOLKSWAGEN,
    NISSAN,
    PEUGEOT,
    CITROEN
  }
  export const size = 5;
  export function labels(): string[] {
    const array = [];
    for (var i = 0; i < size; i++) {
      array.push(getLabel(i));
    }
    return array;
  }
  export function getLabel(instance: e): string {
    switch (instance) {
      case e.AUDI:
        return "Audi";
      case e.VOLKSWAGEN:
        return "Volkswagen";
      case e.NISSAN:
        return "Nissan";
      case e.PEUGEOT:
        return "Peugeot";
      case e.CITROEN:
        return "Citroen";
    }
  }
}
