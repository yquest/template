export namespace selectInput {
  export interface Props {
    selected: number;
    className?: string;
    tabIndex?: number;
    list: string[];
    onChange: (e: React.ChangeEvent<HTMLSelectElement>) => void;
    toKey: (index: number, value: string) => string;
  }
}
