import * as React from "react";
import { uiStore as rUiStore, ModalState, UiStore } from "../../stores/UIStore";
import classNames from "classnames/bind";
import { observer } from "mobx-react";

function onAction(e) {
  uiStore.modalContent.actionEvent(e);
  uiStore.updateModal(ModalState.SAVED);
}

function onCancel() {
  uiStore.updateModal(ModalState.CANCELED);
}

function onTransitionEnd() {
  if (
    uiStore.modal === ModalState.CANCELED ||
    uiStore.modal === ModalState.SAVED
  ) {
    uiStore.updateModal(ModalState.REMOVED);
  }
}
export const uiStore:UiStore = rUiStore;
export function showModal(){
  uiStore.updateModal(ModalState.CREATED);
  setTimeout(() => {
      uiStore.updateModal(ModalState.SHOW);
  }, 500);
}
export const Modal = observer(() => {
  let modalClasses = classNames({
    modal: true,
    fade: true,
    "d-block": true,
    show: uiStore.modal === ModalState.SHOW
  });
  return (
    uiStore.modelInDOM && (
      <div
        onTransitionEnd={onTransitionEnd}
        className={modalClasses}
        tabIndex={-1}>
        <div className="modal-dialog">
          <div className="modal-content">
            <div className="modal-header">
              <h5 className="modal-title">{uiStore.modalContent.title}</h5>
              <button type="button" className="close" onClick={onCancel}>
                <span aria-hidden="true">×</span>
              </button>
            </div>
            <div className="modal-body">{uiStore.modalContent.content}</div>
            <div className="modal-footer">
              <button
                onClick={onCancel}
                type="button"
                className="btn btn-secondary">
                Close
              </button>
              <button
                type="button"
                className="btn btn-primary"
                onClick={onAction}>
                Confirm
              </button>
            </div>
          </div>
        </div>
      </div>
    )
  );
});
