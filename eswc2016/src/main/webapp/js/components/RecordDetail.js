'use strict';

import React from 'react';
import {Button, ButtonToolbar, Glyphicon, Input, Modal} from 'react-bootstrap';
import Typeahead from 'react-bootstrap-typeahead';
import TypeaheadResultList from './TypeaheadResultList';
import Mask from './Mask';

import Actions from '../actions/Actions';
import QuestionStore from '../stores/QuestionStore';

export default class RecordDetail extends React.Component {
    constructor(props) {
        super(props);
        var record = this.props.record;
        this.state = {
            questions: QuestionStore.getQuestions(),
            showAddButton: false,
            question: record.has_question ? record.has_question : null,
            answer: record.has_answer ? record.has_answer : {}
        }
    }

    componentDidMount() {
        this.unsubscribe = QuestionStore.listen(this._onQuestionsLoaded.bind(this));
        if (!this.state.questions) {
            Actions.loadQuestions();
        }
    }

    componentWillUnmount() {
        this.unsubscribe();
    }

    _onQuestionsLoaded(data) {
        if (data.action === Actions.loadQuestions) {
            var questions = data.questions,
                newState = {
                    questions: questions
                };
            if (!this.state.question && this.state.questionIdentifier) {
                // The selected question is a new one
                var identifier = this.state.questionIdentifier;
                for (var i = 0, len = questions.length; i < len; i++) {
                    if (questions[i].identifier === identifier) {
                        newState.question = questions[i];
                        newState.questionIdentifier = null;
                        break;
                    }
                }
            }
            this.setState(newState);
        }
    }

    _onQuestionSelected(question) {
        this.setState({question: question});
    }

    _onAnswerChange(e) {
        var answer = this.state.answer;
        answer.has_data_value = e.target.value;
        this.setState({answer: answer});
    }

    _onQuestionKeyUp(e) {
        var options = this.refs.question.getCurrentOptions();
        if (options.length === 0 && e.target.value !== '') {
            this.setState({showAddButton: true});
        } else {
            this.setState({showAddButton: false});
        }
        return true;
    }

    _onSave() {
        var record = this.props.record;
        record.has_question = this.state.question;
        record.has_answer = this.state.answer;
        this.props.actions.onSave(record);
    }

    _onRemove() {
        this.props.actions.onRemove(this.props.record);
    }

    _addQuestion() {
        var questions = this.state.questions,
            questionValue = this.refs.question.state.entryValue,
            newQuestion = {
                has_data_value: questionValue
            };
        Actions.createQuestion(newQuestion, function (key) {
            this.setState({questionIdentifier: key});
        }.bind(this));
    }

    render() {
        var removeButton = this.props.record.isNew ? null :
                <Button bsStyle='warning' onClick={this._onRemove.bind(this)}>Remove</Button>,
            mask = this.state.questionIdentifier ? <Mask text='Adding question'/> : null;
        return <Modal show={this.props.show} onHide={this.props.actions.onClose}>
            <Modal.Header closeButton>
                <Modal.Title>Create record</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <div className='row'>
                    <label className='col-xs-12 control-label'>Question</label>
                </div>
                <div className='row'>
                    <div className='col-xs-10'>
                        <Typeahead ref='question' className='form-group form-group-sm'
                                   optionsButton={true} placeholder='Type question or select an existing one'
                                   onOptionSelected={this._onQuestionSelected.bind(this)} filterOption='has_data_value'
                                   displayOption='has_data_value' options={this.state.questions}
                                   value={this.state.question ? this.state.question.has_data_value : ''}
                                   onKeyUp={this._onQuestionKeyUp.bind(this)}
                                   customListComponent={TypeaheadResultList}/>
                    </div>
                    <div className='col-xs-2' style={{verticalAlign: 'bottom'}}>{this._renderAddQuestionButton()}</div>
                </div>
                <div className='row'>
                    <div className='col-xs-12'>
                        <Input type='textarea' label='Answer' bsSize='small' value={this.state.answer.has_data_value}
                               onChange={this._onAnswerChange.bind(this)}/>
                    </div>
                </div>
                {mask}
            </Modal.Body>
            <Modal.Footer>
                <ButtonToolbar>
                    <Button bsStyle='success' onClick={this._onSave.bind(this)}>Save</Button>
                    <Button onClick={this.props.actions.onClose}>Cancel</Button>
                    {removeButton}
                </ButtonToolbar>
            </Modal.Footer>
        </Modal>
    }

    _renderAddQuestionButton() {
        if (this.state.showAddButton) {
            return <Button bsStyle='info' bsSize='small' title='Add this question'
                           onClick={this._addQuestion.bind(this)}>
                <Glyphicon glyph='plus'/>
            </Button>;
        }
        return null;
    }
}