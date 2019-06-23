import * as React from "react";
import { uiStore, ModalState } from "../../UIStore";
import classNames from "classnames/bind";

export const createModalOverlay = () => uiStore.modelInDOM && <div className="modal-backdrop fade show" />

function onAction(){
  uiStore.updateModal(ModalState.SAVED);
  uiStore.modalAction();
}

function onCancel(){
  uiStore.updateModal(ModalState.CANCELED);
}

export const createModalContainer = () => {
  if (uiStore.modal === ModalState.CREATED) {
    setTimeout(() => {
      uiStore.updateModal(ModalState.SHOW);
    }, 100);
  }
  let modalClasses = classNames({
    modal: true,
    fade: true,
    show: uiStore.modal === ModalState.SHOW
  });
  return (
    uiStore.modelInDOM && (
      <div
        onTransitionEnd={() => {
          if (
            uiStore.modal === ModalState.CANCELED ||
            uiStore.modal === ModalState.SAVED
          ) {
            uiStore.updateModal(ModalState.REMOVED);
          }
        }}
        className={modalClasses}
        tabIndex={-1}
        style={{ display: "block" }}>
        <div className="modal-dialog">
          <div className="modal-content">
            <div className="modal-header">
              <h5 className="modal-title">{uiStore.modalContent.title}</h5>
              <button type="button" className="close">
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
                Save changes
              </button>
            </div>
          </div>
        </div>
      </div>
    )
  );
};
