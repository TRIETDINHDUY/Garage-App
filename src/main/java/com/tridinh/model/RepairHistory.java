package com.tridinh.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "repair_history")
public class RepairHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long historyId;

    @Column(columnDefinition = "VARCHAR(20)")
    private String oldStatus;

    @Column(columnDefinition = "VARCHAR(20)")
    private String newStatus;

    private LocalDateTime changedDate;
    private String changedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "repair_order_id", nullable = false)
    private RepairOrder repairOrder;

    public RepairHistory() {}

    public Long getHistoryId() { return historyId; }
    public void setHistoryId(Long historyId) { this.historyId = historyId; }

    public String getOldStatus() { return oldStatus; }
    public void setOldStatus(String oldStatus) { this.oldStatus = oldStatus; }

    public String getNewStatus() { return newStatus; }
    public void setNewStatus(String newStatus) { this.newStatus = newStatus; }

    public LocalDateTime getChangedDate() { return changedDate; }
    public void setChangedDate(LocalDateTime changedDate) { this.changedDate = changedDate; }

    public String getChangedBy() { return changedBy; }
    public void setChangedBy(String changedBy) { this.changedBy = changedBy; }

    public RepairOrder getRepairOrder() { return repairOrder; }
    public void setRepairOrder(RepairOrder repairOrder) { this.repairOrder = repairOrder; }
}
