import { observer } from "mobx-react";
import { page2 } from "./components/app/props/Page2Props";
import { stores } from "./stores/Stores";
import { App } from "./components/gen/AppTpl";
import * as React from "react";

export const Root = observer(() => {
  if (stores.router.page === "page2") {
    return page2.createComponent();
  } else {
    return (<App/>);
  }
});
