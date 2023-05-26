package repository

import (
	"testing"
)

func TestConnect(t *testing.T) {
	
	db := &Database{}
	err := db.Connect()

	if err != nil {
		t.Errorf("Failed to connect to DB: %v", err)
	}

	// Check if the DB field is not nil
	if db.DB == nil {
		t.Error("DB field is not initialized")
	}

	
}
