'use strict';

import React from 'react';
import Reflux from 'reflux';
import {Alert} from 'react-bootstrap';
import assign from 'object-assign';

import AuditStore from '../stores/AuditStore';
import AuditDetail from './AuditDetail';
import Actions from '../actions/Actions';
import Routing from '../util/Routing';

export default class AuditController extends React.Component {

    constructor(props) {
        super(props);
        var isNew = props.params.auditKey ? false : true;
        this.state = {
            new: isNew,
            audit: isNew ? {
                isNew: true,
                date: (Date.now() / 1000) * 1000
            } : null,
            showMessage: false,
            message: null
        }
    }

    componentDidMount() {
        this.unsubscribe = AuditStore.listen(this._onAuditLoaded.bind(this));
        if (!this.state.new) {
            Actions.loadAudit(this.props.params.auditKey);
        }
    }

    componentWillUnmount() {
        this.unsubscribe();
    }

    _onAuditLoaded(data) {
        if (data.action !== Actions.loadAudit) {
            return;
        }
        this.setState({audit: data.audit});
    }

    onChange(change) {
        var audit = this.state.audit;
        assign(audit, change);
        this.setState({audit: audit});
    }

    onCancel() {
        Routing.transitionTo('audits');
    }

    onSave() {
        var audit = this.state.audit;
        if (audit.isNew) {
            Actions.createAudit(audit, this.onSaveSuccess.bind(this));
        } else {
            Actions.updateAudit(audit, this.onSaveSuccess.bind(this));
        }
    }

    onSaveSuccess() {
        this.setState({showMessage: true, message: 'Save successful.'});
    }

    _hideMessage() {
        this.setState({showMessage: false});
    }

    render() {
        if (!this.state.audit) {
            return null;
        }
        var actions = {
            change: this.onChange.bind(this),
            cancel: this.onCancel,
            save: this.onSave.bind(this)
        };
        return (
            <div>
                <AuditDetail audit={this.state.audit} actions={actions}/>
                {this._renderMessage()}
            </div>);
    }

    _renderMessage() {
        if (this.state.showMessage) {
            return <Alert bsStyle='success' onDismiss={this._hideMessage.bind(this)} dismissAfter={2000}>
                {this.state.message}
            </Alert>;
        }
    }
}
