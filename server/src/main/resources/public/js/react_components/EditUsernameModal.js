/**
 * Created by manasb on 28-11-2016.
 */

let React = require("react");
let EventBus = require("eventbusjs");
let EventBusClientEvents = require("../eventbus_client_events");
let MessageType = require("../domain/MessageType");
let Config = require("../../config.json");

class EditUsernameModal extends React.Component {

    constructor() {
        super();

        this.state = {
            currentUser: undefined
        };

        EventBus.addEventListener(MessageType.IDENTITY, this.onIdentityMessage, this);
        EventBus.addEventListener(EventBusClientEvents.EDIT_USERNAME, this.showModal, this);
    }

    onIdentityMessage(message) {
        this.setState({
            currentUser: message.target.data
        });
    }

    showModal() {
        $(this.refs.errorContainer).hide();
        $(this.refs.usernameInput).val(this.state.currentUser.username);
        $(this.refs.modal).modal("show");
    }

    hideModal() {
        $(this.refs.modal).modal("hide");
    }

    onCloseButtonClick(event) {
        event.preventDefault();
        this.hideModal();
    }

    onSaveButtonClick(event) {
        event.preventDefault();

        const newUsername = $(this.refs.usernameInput).val();
        let self = this;

        $.ajax({
            type: "POST",
            url: "http://" + window.location.hostname + ":" + Config.port + "/edit-username",
            data: this.state.currentUser.username + "," + newUsername,
            success: function (data) {
                data = JSON.parse(data);
                if (data.statusCode == 200) {
                    $(self.refs.errorContainer).hide();
                    self.hideModal();
                } else {
                    $(self.refs.errorContainer).show();
                    $(self.refs.errorText).text(data.error);
                }
            }
        });
    }

    render() {
        return (
            <div ref="modal" id="edit-username-modal" className="modal fade" tabIndex="-1" role="dialog">
                <div className="modal-dialog" role="document">
                    <div className="modal-content">
                        <div className="modal-header">
                            <button type="button" className="close" ><span>&times;</span></button>
                            <h4 className="modal-title">Edit Username</h4>
                        </div>
                        <div className="modal-body">
                            <div ref="errorContainer" className="alert alert-danger" role="alert">
                                <span><strong>Error: </strong></span><span ref="errorText"/>
                            </div>
                            <input ref="usernameInput" type="text" className="form-control" placeholder="Type your username here"/>
                        </div>
                        <div className="modal-footer">
                            <button type="button" className="btn btn-default"
                                    onClick={(event) => this.onCloseButtonClick(event)}>Close</button>
                            <button type="button" className="btn btn-primary"
                                    onClick={(event) => this.onSaveButtonClick(event)}>Save</button>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

module.exports = EditUsernameModal;