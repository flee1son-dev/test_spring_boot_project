package com.flee1son_dev.homework_spring;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

@Service 
public class TaskService {
    private static final HashMap<Long, Task> taskMap = new HashMap<>(Map.of(
        1L, new Task(
            1L,
            21L, 
            132L, 
            TaskStatus.IN_PROGRESS, 
            LocalDateTime.now(), 
            LocalDateTime.now().plusDays(7), 
            TaskPriority.MEDIUM),
        2L, new Task(
            2L,
            35L, 
            12L, 
            TaskStatus.IN_PROGRESS, 
            LocalDateTime.now(), 
            LocalDateTime.now().plusDays(2), 
            TaskPriority.HIGH),
        3L, new Task(
            3L,
            495L, 
            1031L, 
            TaskStatus.IN_PROGRESS, 
            LocalDateTime.now(), 
            LocalDateTime.now().plusDays(14), 
            TaskPriority.LOW),
        4L, new Task(
            4L,
            3212L, 
            987L, 
            TaskStatus.IN_PROGRESS, 
            LocalDateTime.now(), 
            LocalDateTime.now().plusDays(14), 
            TaskPriority.LOW)
    ));

    public Task getTaskById(Long id){
        if(!taskMap.containsKey(id)){
            throw new NoSuchElementException("Not fount task by id= " + id);
        }
        return taskMap.get(id);
    }

    public List<Task> findAllTasks(){
        return taskMap.values().stream().toList();
    }
}
