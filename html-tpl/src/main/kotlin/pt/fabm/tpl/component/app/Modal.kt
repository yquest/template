package pt.fabm.tpl.component.app

import pt.fabm.tpl.Component
import pt.fabm.tpl.NoTagElement
import pt.fabm.tpl.TextElement
import pt.fabm.tpl.Type

class Modal(type: Type) : Component("Modal", type) {
  init {
    val clientContent = {
    """
    import * as React from "react";
    import { uiStore as rUiStore, ModalState, UiStore } from "../../UIStore";
    import classNames from "classnames/bind";
    import { observer } from "mobx-react";
    
    function onAction() {
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
      setTimeout(() => {
          uiStore.updateModal(ModalState.CREATED);
      }, 100);
      setTimeout(() => {
          uiStore.updateModal(ModalState.SHOW);
      }, 500);
    }
    export const Modal = observer(() => {
      let modalClasses = classNames({
        modal: true,
        fade: true,
        "display-block": true,
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
                    Confirm
                  </button>
                </div>
              </div>
            </div>
          </div>
        )
      );
    });
    
    """.trimIndent()
    }

    if (type == Type.CLIENT_IMPLEMENTATION) {
      children += NoTagElement(listOf(TextElement(clientContent())))
        .toElementCreator(type)
    }
  }
}
