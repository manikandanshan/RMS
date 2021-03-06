
package com.gits.rms.service;

import java.util.Date;
import java.util.List;

import com.gits.rms.vo.TimesheetNotesVO;

public interface TimeSheetNotesService {

    TimesheetNotesVO getTimeSheetNotes(String date);
    
    List getTimeSheetNotesList(Date startingDate,Date endDate,int empid);

    void insertOrUpdateTimesheetNotes(TimesheetNotesVO notesObj);
    
    void updateTimesheetNotes(TimesheetNotesVO notesObj);
    
    TimesheetNotesVO getEmpTimeSheetNotes(String date,int empId);

}
