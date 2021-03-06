package com.gits.rms.persistence.performance;

import java.util.List;

import com.gits.rms.vo.EmployeesVO;
import com.gits.rms.vo.NationalityVO;
import com.gits.rms.vo.QuestionBankVO;
import com.gits.rms.vo.QuestionGroupNameIdentificationVO;

public interface QuestionBankDao {
    
    List getAllQuestions();
    
    void insertQuestionBank(QuestionBankVO quesBank);
    
    List getAllQuestionBankName();
    
    void insertQuestionGroup(QuestionGroupNameIdentificationVO quesGroupId);
    
    List checkQuestionBank(QuestionGroupNameIdentificationVO quesGroupId);
    
    QuestionGroupNameIdentificationVO getQuestionGroupNameIdentificationVO(Integer id);
    
    EmployeesVO getEmployeeId(String empFullName);
    
    List getAllQuestionBank(Integer id);
    
    QuestionBankVO getQuestionBank(Integer id);
    
    List getAnswerCountForQuestion(Integer id);
    
    List getAllQuestionGroupNameIdentification();
}
