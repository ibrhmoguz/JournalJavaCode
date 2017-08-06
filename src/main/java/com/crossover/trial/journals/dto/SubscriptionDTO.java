package com.crossover.trial.journals.dto;

import com.crossover.trial.journals.model.Category;

public class SubscriptionDTO {

    private long id;

    private String name;

    private boolean active;

    public SubscriptionDTO(Category c) {
        name = c.getName();
        id = c.getId();
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o){ 
        	return true;
        }
        if (o == null || getClass() != o.getClass()) {
        	return false;
        }

        SubscriptionDTO that = (SubscriptionDTO) o;

        return id == that.id;
    }
    
}
