package com.csci3130.project.maximegalonline;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/** This class functions as a bridge between the course section data and the RecyclerView
 * that displays it
 *
 *
 */

import com.csci3130.project.maximegalonline.course.CourseSection;
import com.csci3130.project.maximegalonline.register.CourseDrop;
import com.csci3130.project.maximegalonline.register.CourseRegistrationUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**SectionRVAdapter
 *
 * Adapter for handling view for courses.
 *
 * @author Dawson Wilson
 * @author Aecio Cavalcanti
 * @author Peter Lee
 * @author Megan Gosse
 * @author Joanna Bistekos
 */
public class SectionRVAdapter extends RecyclerView.Adapter<SectionRVAdapter.SectionViewHolder>{

    List<CourseSection> sections;

    /** Constructor
     *
     * @param sections A list containing CourseSection objects
     */
    SectionRVAdapter(List<CourseSection> sections) {
        this.sections = sections;
    }

    /** SectionViewHolder
     * provides a way to access data
     */
    public static class SectionViewHolder extends RecyclerView.ViewHolder {
        TextView section_capacity;
        TextView section_enrolled;
        TextView section_crn;
        TextView section_prof;
        TextView section_id;
        Button register_button;
	    Button drop_button;
        RecyclerView times_rv;
        List<CourseSection> sections;
        Context applicationContext;

        /** Constructor
         *
         * @param itemView each item/"row" on the RecyclerView list
         * @param sections A list containing CourseSection objects
         */
        SectionViewHolder(View itemView, final List<CourseSection> sections) {
            super(itemView);
            this.sections = sections;
            section_enrolled = itemView.findViewById(R.id.section_enrolled);
            section_capacity = itemView.findViewById(R.id.section_capacity);
            section_crn = itemView.findViewById(R.id.section_crn);
            section_prof = itemView.findViewById(R.id.section_professor);
            section_id = itemView.findViewById(R.id.section_id);
            register_button = itemView.findViewById(R.id.register_button);
	        drop_button = itemView.findViewById(R.id.drop_button);
            times_rv = itemView.findViewById(R.id.section_times_rv);

            applicationContext = itemView.getContext().getApplicationContext();

            register_button.setOnClickListener(new View.OnClickListener() {

                /**
                 * manager for clicking on register button
                 **/
                @Override
                public void onClick(View v) {
                // Here we would make a call to the database to register for a course
                int position = getAdapterPosition();

                // Update current CourseRegistrationUI
                CourseRegistrationUI coursereg = null;
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

		        coursereg = new CourseRegistrationUI();
                CourseSection cs = sections.get(position);
                Log.d("SECTIONRV","values of sometimes broken if " + cs.getcourse().getsemester()
                            + " " + cs.getcourse().getyear());

                coursereg.firebaseRegister(currentUser, cs, applicationContext);
                
                }
            });

            drop_button.setOnClickListener(new View.OnClickListener() {
                /**
                 * manager for clicking on drop button
                 **/
                @Override
                public void onClick(View v) {
                int position = getAdapterPosition();
                CourseSection cs = sections.get(position);
                CourseDrop cd = new CourseDrop(applicationContext);
                cd.drop(cs.getcrn());
                }
            });

            Intent courseRVIntent = ((Activity) itemView.getContext()).getIntent();
            Bundle courseRVBundle = courseRVIntent.getExtras();
            if (courseRVBundle != null) {
                if (courseRVBundle.get("prevActivity").equals("Main")) register_button.setVisibility(View.GONE);
                else if (courseRVBundle.get("prevActivity").equals("AvailCourses")) drop_button.setVisibility(View.GONE);
            }
        }
    }

    /**
     * Creates and returns a SectionViewHolder to be used by onBindViewHolder
     *
     * @param viewGroup
     * @param i
     * @return a SectionViewHolder
     */
    @Override
    public SectionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Log.d("RV", "Creating card");
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.section_list, viewGroup, false);
        SectionViewHolder pvh = new SectionViewHolder(v, this.sections);
        return pvh;
    }

    /**
     * This method is called by RecyclerView to display the data at the specified position
     * it assigns the data from a CourseSection object to the fields on the corresponding RecyclerView
     * row
     *
     * @param sectionViewHolder
     * @param i the position of a item on the sections list
     */
    @Override
    public void onBindViewHolder(SectionViewHolder sectionViewHolder, int i) {
        // This refers to the public class Courses
        Log.d("SECTION","Section will have these values: " + sections.get(i).getcrn() + " " + sections.get(i).getprofessor());
        sectionViewHolder.section_crn.setText(sections.get(i).getcrn());
        sectionViewHolder.section_enrolled.setText(sections.get(i).getenrolled()+"");
        sectionViewHolder.section_capacity.setText(sections.get(i).getcapacity()+"");
        sectionViewHolder.section_prof.setText(sections.get(i).getprofessor());
        sectionViewHolder.section_id.setText(sections.get(i).getsectionNum());

        //Setting up the inner recycler view
        SectionTimesRVAdapter adapter = new SectionTimesRVAdapter(sections.get(i).getcourseTimeList());
        sectionViewHolder.times_rv.setAdapter(adapter);
        sectionViewHolder.times_rv.setHasFixedSize(true);
    }

    /**
     * Gets item count
     *
     * @return number of items on the RecyclerView
     */
    @Override
    public int getItemCount() {
        return sections.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
