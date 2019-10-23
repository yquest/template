import styled from "styled-components";
import { Calendar } from "./Calendar";

export const StyledCalendar = styled(Calendar)`
  height: ${props => (props.open ? "20rem" : "0")};
  transition-timing-function: ease-in-out;
  transition: all 1s;
`;